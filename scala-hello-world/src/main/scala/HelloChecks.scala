import com.sun.net.httpserver.HttpServer
import java.io.{ByteArrayOutputStream, PrintStream}
import java.net.{HttpURLConnection, InetSocketAddress, URL}
import java.nio.charset.StandardCharsets
import scala.util.Try

case class Check(name: String, run: () => Boolean)

case class CheckResult(name: String, passed: Boolean)

object HelloChecks:
  def escapeHtml(text: String): String =
    text
      .replace("&", "&amp;")
      .replace("<", "&lt;")
      .replace(">", "&gt;")
      .replace("\"", "&quot;")
      .replace("'", "&#39;")

  def renderHtml(results: Seq[CheckResult]): String =
    val total = results.size
    val passed = results.count(_.passed)
    val failed = total - passed
    val allPassed = total > 0 && failed == 0
    val status = if allPassed then "PASSING" else "FAILING"

    val resultsHtml = results.zipWithIndex.map { (result, index) =>
      val statusText = if result.passed then "PASS" else "FAIL"
      val escapedName = escapeHtml(result.name)
      s"""<li><strong>$escapedName</strong>: $statusText</li>"""
    }.mkString("\n    ")

    s"""<!DOCTYPE html>
       |<html lang="en">
       |<head>
       |  <meta charset="UTF-8">
       |  <title>Hello World Checks</title>
       |</head>
       |<body>
       |  <h1>Hello World</h1>
       |  <h2>Status: $status</h2>
       |  <p>Total: $total, Passed: $passed, Failed: $failed</p>
       |  <ol>
       |    $resultsHtml
       |  </ol>
       |</body>
       |</html>""".stripMargin

  def startServer(port: Int, html: String): HttpServer =
    val server = HttpServer.create(new InetSocketAddress("localhost", port), 0)
    server.createContext("/", exchange =>
      if exchange.getRequestURI.getPath == "/" then
        exchange.getResponseHeaders.set("Content-Type", "text/html; charset=UTF-8")
        val bytes = html.getBytes(StandardCharsets.UTF_8)
        exchange.sendResponseHeaders(200, bytes.length)
        val os = exchange.getResponseBody
        try os.write(bytes)
        finally os.close()
      else
        exchange.getResponseHeaders.set("Content-Type", "text/plain; charset=UTF-8")
        val bytes = "Not Found".getBytes(StandardCharsets.UTF_8)
        exchange.sendResponseHeaders(404, bytes.length)
        val os = exchange.getResponseBody
        try os.write(bytes)
        finally os.close()
    )
    server.start()
    server

  def withTemporaryServer[T](html: String)(f: Int => T): T =
    val server = startServer(0, html)
    try
      val port = server.getAddress.getPort
      f(port)
    finally
      server.stop(0)

  def httpRequest(url: URL, timeoutMs: Int = 5000): Try[(Int, String, String, Int)] =
    Try {
      val conn = url.openConnection().asInstanceOf[HttpURLConnection]
      try
        conn.setConnectTimeout(timeoutMs)
        conn.setReadTimeout(timeoutMs)
        conn.setRequestMethod("GET")
        val status = conn.getResponseCode
        val contentType = conn.getContentType
        val contentLength = conn.getContentLength
        val is = if status >= 200 && status < 300 then conn.getInputStream else conn.getErrorStream
        val bytes = if is != null then Try(is.readAllBytes()).getOrElse(Array.emptyByteArray) else Array.emptyByteArray
        val body = new String(bytes, StandardCharsets.UTF_8)
        (status, contentType, body, contentLength)
      finally
        conn.disconnect()
    }

  val checks: List[Check] = List(
    Check("Greeting matches 'Hello World' exactly", () => Hello.helloWorld() == "Hello World"),

    Check("Console output contains one greeting and the platform newline", () => {
      val baos = new ByteArrayOutputStream()
      val ps = new PrintStream(baos, true, StandardCharsets.UTF_8.name())
      Hello.printGreeting(ps)
      val output = baos.toString(StandardCharsets.UTF_8.name())
      output == "Hello World" + System.lineSeparator()
    }),

    Check("Repeated calls return the same greeting", () => {
      Hello.helloWorld() == Hello.helloWorld() && Hello.helloWorld() == "Hello World"
    }),

    Check("The rendered page displays the greeting", () => {
      val results = Seq(CheckResult("dummy", true))
      val html = renderHtml(results)
      html.contains("<h1>Hello World</h1>")
    }),

    Check("Passing results display the correct status and totals", () => {
      val results = Seq(CheckResult("test1", true), CheckResult("test2", true))
      val html = renderHtml(results)
      html.contains("Status: PASSING") && html.contains("Total: 2") && html.contains("Passed: 2") && html.contains("Failed: 0")
    }),

    Check("Failed results display failure and the correct totals", () => {
      val results = Seq(CheckResult("test1", true), CheckResult("test2", false))
      val html = renderHtml(results)
      html.contains("Status: FAILING") && html.contains("Total: 2") && html.contains("Passed: 1") && html.contains("Failed: 1")
    }),

    Check("Each individual result displays its name and PASS or FAIL status", () => {
      val results = Seq(CheckResult("test1", true), CheckResult("test2", false))
      val html = renderHtml(results)
      html.contains("test1") && html.contains("PASS") && html.contains("test2") && html.contains("FAIL")
    }),

    Check("Empty results do not report PASSING", () => {
      val results = Seq.empty[CheckResult]
      val html = renderHtml(results)
      !html.contains("PASSING") && html.contains("FAILING") && html.contains("Total: 0")
    }),

    Check("Test names are escaped as HTML text", () => {
      val results = Seq(CheckResult("<script>alert('xss')</script>", true))
      val html = renderHtml(results)
      !html.contains("<script>") && html.contains("&lt;script&gt;") && html.contains("&#39;xss&#39;")
    }),

    Check("HTTP / returns status 200 and the expected page", () => {
      val results = Seq(CheckResult("dummy", true))
      val html = renderHtml(results)
      withTemporaryServer(html) { port =>
        val url = new URL(s"http://localhost:$port/")
        httpRequest(url).map { case (status, _, body, _) =>
          status == 200 && body.contains("<h1>Hello World</h1>")
        }.getOrElse(false)
      }
    }),

    Check("HTTP responses declare UTF-8 HTML and the correct byte length", () => {
      val results = Seq(CheckResult("dummy", true))
      val html = renderHtml(results)
      withTemporaryServer(html) { port =>
        val url = new URL(s"http://localhost:$port/")
        httpRequest(url).map { case (status, contentType, body, contentLength) =>
          val bytes = html.getBytes(StandardCharsets.UTF_8)
          status == 200 &&
          contentType != null && contentType.contains("text/html") &&
          contentType.contains("UTF-8") &&
          contentLength == bytes.length
        }.getOrElse(false)
      }
    }),

    Check("Unknown paths return status 404 and 'Not Found'", () => {
      val results = Seq(CheckResult("dummy", true))
      val html = renderHtml(results)
      withTemporaryServer(html) { port =>
        val url = new URL(s"http://localhost:$port/unknown")
        httpRequest(url).map { case (status, _, body, _) =>
          status == 404 && body == "Not Found"
        }.getOrElse(false)
      }
    })
  )

  def runChecks(): Seq[CheckResult] =
    checks.map { check =>
      val passed = Try(check.run()).getOrElse(false)
      CheckResult(check.name, passed)
    }
