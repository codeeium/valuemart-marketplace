# Scala Hello World

Prints `Hello World` and serves a local page showing twelve shared automated
check results. Requires JDK 17+ and sbt; uses Scala 3.3.8 and sbt 1.9.9.
The first build downloads Scala and ScalaTest dependencies.

```sh
cd /Users/developer1/WebstormProjects/ivaluemart-marketplace./scala-hello-world
sbt compile
sbt test
sbt run
```

Open http://localhost:3001. Stop with Ctrl+C. The server binds to localhost;
port 3001 must be available. Unknown paths return HTTP 404 and `Not Found`.
Checks run once at startup; restart the application to refresh their results.

The application and ScalaTest FunSuite use the same twelve checks: greeting,
console output, repeatability, page rendering, pass/fail totals, individual
statuses, empty results, HTML escaping, HTTP root, UTF-8 byte lengths, and 404s.
HTTP checks use temporary ports and close their resources afterward.

The JDK built-in HttpServer is the only HTTP implementation. ScalaTest 3.2.17
is a test-only dependency. No additional application dependencies are used.
Project and workflow instructions are saved in CLAUDE.md.

The page labels its startup results as Checks, distinct from the full ScalaTest
suite, and explains that refreshing the page does not rerun them. Empty results
show an explicit message. Long names and failure diagnostics wrap within the page.

The page displays an ordered list of PASS/FAIL results and total, passed, and
failed counts. Overall status is PASSING only for nonempty, entirely passing
results; otherwise it is FAILING. Failure diagnostics are retained and escaped
as HTML alongside check names. Responses use UTF-8 and byte-based lengths.
The complete suite includes the twelve shared checks and a regression test for
retaining and safely displaying failure diagnostics.
