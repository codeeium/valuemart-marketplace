# Scala Hello World

A simple Scala application that displays "Hello World" and runs automated checks via an HTTP server.

## What It Does

- Returns the string "Hello World"
- Prints the greeting to the console
- Starts an HTTP server on localhost:3001
- Displays automated check results on a web page
- Shows pass/fail status for each check
- Returns 404 for unknown paths

## Prerequisites

- JDK 17 or newer
- sbt 1.9.9

## Scala Version

Scala 3.3.8

## First Build

The first build will download dependencies including ScalaTest 3.2.17. This may take a few minutes depending on your network connection.

## How to Compile

```sh
cd /Users/developer1/WebstormProjects/ivaluemart-marketplace./scala-hello-world
sbt compile
```

## How to Run Tests

```sh
sbt test
```

Tests use ScalaTest FunSuite and run the shared checks defined in `HelloChecks.scala`.

## How to Start and Stop the Application

Start the application:

```sh
sbt run
```

The server will run at http://localhost:3001

Stop the application by pressing Ctrl+C in the terminal.

## Test Results

Test results are generated at application startup by running the shared checks in `HelloChecks.scala`. The checks are executed once and their results are displayed on the web page. Restarting the application reruns the checks.

The shared-check design ensures that both the automated tests (in `HelloTest.scala`) and the web page display use the same verification logic.

## Unknown Path Behavior

Requests to paths other than `/` return HTTP 404 with the body "Not Found".
