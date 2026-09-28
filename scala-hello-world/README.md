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
