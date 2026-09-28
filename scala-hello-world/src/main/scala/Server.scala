import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress
import java.nio.charset.StandardCharsets.UTF_8
import java.util.concurrent.CountDownLatch

object Server {
  val port = 3001

  private def escape(text: String): String =
    text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")

  def page(results: Seq[HelloChecks.Result]): String = {
    val passed = results.count(_.passed)
    val failed = results.size - passed
    val status = if (results.nonEmpty && failed == 0) "PASSING" else "FAILING"
    val rows = results.map { result =>
      val name = escape(result.name)
      val detail = result.diagnostic.map(text => s"<pre>${escape(text)}</pre>").getOrElse("")
      s"<li>$name: <strong>${if (result.passed) "PASS" else "FAIL"}</strong>$detail</li>"
    }.mkString("\n")
    s"""<!doctype html>
       |<html lang="en"><head><meta charset="utf-8">
       |<meta name="viewport" content="width=device-width, initial-scale=1">
       |<title>Hello World Tests</title>
       |<style>body {font: 18px system-ui; max-width: 760px; margin: 48px auto; padding: 24px; background: #f5f7fa; color: #182333} main {background: white; padding: 32px; border-radius: 12px} li {margin: 12px 0} .status {font-weight: bold}</style>
       |</head><body><main><h1>${Hello.helloWorld()}</h1>
       |<p class="status">$status</p>
       |<p>${results.size} Tests</p><p>$passed Passed</p><p>$failed Failed</p>
       |<ol>$rows</ol></main></body></html>""".stripMargin
  }

  // Port zero selects an available port for tests.
  def start(port: Int, html: String): HttpServer = {
    val server = HttpServer.create(new InetSocketAddress("localhost", port), 0)
    server.createContext("/", exchange => {
      try {
        val found = exchange.getRequestURI.getPath == "/"
        val body = (if (found) html else "Not Found").getBytes(UTF_8)
        exchange.getResponseHeaders.set("Content-Type", "text/html; charset=utf-8")
        exchange.sendResponseHeaders(if (found) 200 else 404, body.length)
        exchange.getResponseBody.write(body)
      } finally exchange.close()
    })
    server.start()
    server
  }

  def main(args: Array[String]): Unit = {
    Hello.main(args)
    val server = start(port, page(HelloChecks.runAll()))
    Runtime.getRuntime.addShutdownHook(new Thread(() => server.stop(0)))
    println(s"Server running at http://localhost:$port")
    new CountDownLatch(1).await()
  }
}
