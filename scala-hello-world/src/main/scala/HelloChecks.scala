import java.io.{ByteArrayOutputStream, PrintStream}
import java.net.{HttpURLConnection, URI}
import java.nio.charset.StandardCharsets.UTF_8
import scala.util.control.NonFatal

object HelloChecks {
  case class Check(name: String, verify: () => Unit) {
    def run(): Result = {
      try {
        verify()
        Result(name, true)
      } catch {
        case NonFatal(error) =>
          val diagnostic = s"${error.getClass.getSimpleName}: ${Option(error.getMessage).getOrElse("No error message")}"
          System.err.println(s"FAIL: $name: $diagnostic")
          Result(name, false, Some(diagnostic))
      }
    }
  }
  case class Result(name: String, passed: Boolean, diagnostic: Option[String] = None)
  private val passing = Seq(Result("Example check — café", true))

  private def request(path: String)(verify: (HttpURLConnection, String) => Unit): Unit = {
    val server = Server.start(0, Server.page(passing))
    try {
      val connection = URI.create(s"http://localhost:${server.getAddress.getPort}$path")
        .toURL.openConnection().asInstanceOf[HttpURLConnection]
      connection.setConnectTimeout(5000)
      connection.setReadTimeout(5000)
      try {
        val stream = if (connection.getResponseCode < 400) connection.getInputStream else connection.getErrorStream
        val body = try new String(stream.readAllBytes(), UTF_8) finally stream.close()
        verify(connection, body)
      } finally connection.disconnect()
    } finally server.stop(0)
  }

  val checks: Seq[Check] = Seq(
    Check("Greeting matches exactly", () => assert(Hello.helloWorld() == "Hello World")),
    Check("Console prints one greeting and newline", () => {
      val bytes = new ByteArrayOutputStream()
      val out = new PrintStream(bytes, true, UTF_8)
      try Hello.printGreeting(out) finally out.close()
      assert(bytes.toString(UTF_8) == "Hello World" + System.lineSeparator())
    }),
    Check("Repeated calls return the same greeting", () => assert((1 to 100).forall(_ => Hello.helloWorld() == "Hello World"))),
    Check("Page displays the greeting", () => assert(Server.page(passing).contains("<h1>Hello World</h1>"))),
    Check("Passing results show accurate totals", () => {
      val html = Server.page(passing)
      assert(Seq("PASSING", "1 Checks", "1 Passed", "0 Failed").forall(html.contains))
    }),
    Check("Failed results show failure and accurate totals", () => {
      val html = Server.page(passing :+ Result("Failed check", false))
      assert(Seq("FAILING", "2 Checks", "1 Passed", "1 Failed").forall(html.contains))
      assert(!html.contains("PASSING"))
    }),
    Check("Page lists each individual test status", () => {
      val html = Server.page(passing :+ Result("Failed check", false))
      assert(html.contains("Example check — café: <strong>PASS</strong>"))
      assert(html.contains("Failed check: <strong>FAIL</strong>"))
    }),
    Check("Empty results do not report passing", () => {
      val html = Server.page(Seq.empty)
      assert(Seq("FAILING", "0 Checks", "0 Passed", "0 Failed").forall(html.contains))
      assert(!html.contains("PASSING"))
      assert(html.contains("No check results are available."))
    }),
    Check("Test names are escaped as HTML text", () => {
      val html = Server.page(Seq(Result("<script>alert('test')</script> & café", true)))
      assert(html.contains("&lt;script&gt;alert('test')&lt;/script&gt; &amp; café"))
      assert(!html.contains("<script>"))
    }),
    Check("HTTP root serves the Hello World page", () => request("/") { (connection, body) =>
      assert(connection.getResponseCode == 200)
      assert(body == Server.page(passing))
    }),
    Check("HTTP response declares UTF-8 HTML and correct length", () => request("/") { (connection, body) =>
      assert(connection.getContentType == "text/html; charset=utf-8")
      assert(connection.getContentLength == body.getBytes(UTF_8).length)
    }),
    Check("Unknown pages return HTTP 404", () => request("/missing") { (connection, body) =>
      assert(connection.getResponseCode == 404)
      assert(body == "Not Found")
    })
  )

  def runAll(): Seq[Result] = checks.map(_.run())
}
