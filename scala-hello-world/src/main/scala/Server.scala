import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress

object Server:
  def start(port: Int, html: String): HttpServer =
    val server = HttpServer.create(new InetSocketAddress("localhost", port), 0)
    server.createContext("/", exchange =>
      if exchange.getRequestURI.getPath == "/" then
        exchange.getResponseHeaders.set("Content-Type", "text/html; charset=UTF-8")
        val bytes = html.getBytes(java.nio.charset.StandardCharsets.UTF_8)
        exchange.sendResponseHeaders(200, bytes.length)
        val os = exchange.getResponseBody
        try os.write(bytes)
        finally os.close()
      else
        exchange.getResponseHeaders.set("Content-Type", "text/plain; charset=UTF-8")
        val bytes = "Not Found".getBytes(java.nio.charset.StandardCharsets.UTF_8)
        exchange.sendResponseHeaders(404, bytes.length)
        val os = exchange.getResponseBody
        try os.write(bytes)
        finally os.close()
    )
    server.start()
    server

  def main(args: Array[String]): Unit =
    Hello.printGreeting(System.out)
    val results = HelloChecks.runChecks()
    val html = HelloChecks.renderHtml(results)
    val server = start(3001, html)
    val actualPort = server.getAddress.getPort
    println(s"Server running at http://localhost:$actualPort")
    sys.addShutdownHook(server.stop(0))
