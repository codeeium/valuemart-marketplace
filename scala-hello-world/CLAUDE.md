# Scala Hello World — Complete Development Workflow

You are an expert Scala development agent. Recreate the missing
`scala-hello-world` project, then build, test, debug, document, review,
and run it. Work directly on the project and verify your results.

## 1. Project Location

The parent workspace is:

/Users/developer1/WebstormProjects/ivaluemart-marketplace.

The final dot is part of the directory name.

Create or restore the Scala project at:

/Users/developer1/WebstormProjects/ivaluemart-marketplace./scala-hello-world

Inspect the workspace first. If the Scala project already exists, read it
and preserve existing work rather than overwriting it.

Do not modify the separate JavaScript `hello-world` project or unrelated files.

## 2. Application Requirements

Create a Scala Hello World application that:

* Returns the exact string `Hello World`.
* Prints `Hello World` followed by a newline to the console.
* Starts an HTTP server at http://localhost:3001.
* Binds the server to localhost.
* Displays `Hello World` as the page heading.
* Displays individual automated check results.
* Displays the total number of checks, passed checks, and failed checks.
* Displays `PASSING` only when results are nonempty and all checks pass.
* Displays `FAILING` when any check fails or results are empty.
* Returns HTTP 404 with `Not Found` for unknown paths.
* Escapes test names before inserting them into HTML.
* Uses UTF-8 responses and correct response content lengths.

Run the shared checks once at application startup and display their results.
Restarting the application should rerun the checks.

## 3. Technology and Configuration

Use the previously verified project configuration:

* JDK 17 or newer.
* Scala 3.3.8.
* sbt 1.9.9.
* ScalaTest FunSuite 3.2.17 for automated tests.
* The JDK built-in `com.sun.net.httpserver.HttpServer`.
* No additional application or HTTP dependencies.

Configure `Server` as the main class for `sbt run`.
Run the application in a forked JVM.

Use this project structure:

scala-hello-world/
├── CLAUDE.md
├── README.md
├── .gitignore
├── build.sbt
├── project/
│   └── build.properties
└── src/
    ├── main/
    │   └── scala/
    │       ├── Hello.scala
    │       ├── HelloChecks.scala
    │       └── Server.scala
    └── test/
        └── scala/
            └── HelloTest.scala

Save these project instructions in `CLAUDE.md`.

Ignore generated build directories, including:

target/
project/target/
.bsp/
.scala-build/

## 4. Source Responsibilities

### Hello.scala

Provide:

* `helloWorld(): String`, returning `Hello World`.
* `printGreeting(out: java.io.PrintStream): Unit`, printing the greeting
  followed by a newline.
* A main method that prints the greeting.

### HelloChecks.scala

Define shared checks and their results so the application and ScalaTest
use the same verification logic.

Include:

* A named check with executable verification logic.
* A result containing the check name, pass/fail status, and useful failure diagnostics.
* A collection of checks.
* A method to execute all checks and return results.

Report check failures with useful diagnostic information.

HTTP checks must use temporary servers on available ports and release
their connections, streams, and servers after execution.

Use connection and read timeouts so tests do not hang indefinitely.

### Server.scala

Provide:

* HTML rendering from a sequence of check results.
* A server-starting method accepting a port and rendered HTML.
* Support for port zero so tests can request an available temporary port.
* A main method that prints the greeting, runs the shared checks,
  starts the server on port 3001, and reports its URL.
* Shutdown cleanup for the HTTP server.

Keep the page simple and readable, with a greeting, overall status,
totals, and an ordered list of individual results.

### HelloTest.scala

Use ScalaTest FunSuite to register each shared check as an individual test.

## 5. Required Automated Checks

Implement at least the twelve checks from the previously verified project:

1. Greeting matches `Hello World` exactly.
2. Console output contains one greeting and the platform newline.
3. Repeated calls return the same greeting.
4. The rendered page displays the greeting.
5. Passing results display the correct status and totals.
6. Failed results display failure and the correct totals.
7. Each individual result displays its name and PASS or FAIL status.
8. Empty results do not report PASSING.
9. Test names are escaped as HTML text, including `<`, `>`, and `&`,
   while preserving Unicode text.
10. HTTP `/` returns status 200 and the expected page.
11. HTTP responses declare UTF-8 HTML and the correct byte length.
12. Unknown paths return status 404 and `Not Found`.

Add other meaningful tests only if implementation changes warrant them.

## 6. Understand and Plan

Before making significant changes:

* Inspect the project structure.
* Read relevant source, configuration, tests, and documentation.
* Inspect Git status.
* Identify files to create or modify.
* Identify implementation steps and compatibility risks.
* State any necessary assumptions.
* Keep the solution simple and maintainable.
* Reuse existing code and dependencies where appropriate.

## 7. Implement

* Implement all requested functionality completely.
* Follow the existing project's coding style.
* Use appropriate modern Scala features.
* Avoid unnecessary dependencies.
* Keep code readable and responsibilities clear.
* Do not leave placeholders or incomplete implementations.
* Do not change unrelated code.

## 8. Build and Test

Run:

```sh
cd /Users/developer1/WebstormProjects/ivaluemart-marketplace./scala-hello-world
sbt compile
sbt test
```

Inspect the actual output.

If the build or tests fail:

1. Reproduce the failure.
2. Inspect the error and relevant code.
3. Identify the root cause.
4. Fix the underlying problem.
5. Rerun the relevant checks.
6. Run the complete test suite again.

Do not suppress errors or weaken tests to make them pass.
Continue until the build and all relevant tests pass, or report a concrete
environmental blocker honestly.

## 9. Run and Verify

Start the application:

```sh
sbt run
```

Verify:

* The console prints `Hello World`.
* The application starts successfully.
* http://localhost:3001 returns HTTP 200.
* The page displays the greeting.
* All twelve required checks pass.
* Totals are accurate.
* An unknown path returns HTTP 404.

If port 3001 is occupied, identify the process before taking action.
Do not terminate an unrelated application.

If startup fails, inspect and fix the cause, restart, and verify again.

## 10. Code Review and Refactoring

Review for:

* Bugs and incorrect error handling.
* Unnecessary complexity and duplication.
* Poor naming and unused code.
* Resource leaks.
* Missing tests.
* Maintainability problems.

Fix genuine issues. Refactor only when it improves the implementation.
Run the tests after code changes.

## 11. Security Review

Review relevant code for:

* Hard-coded secrets or exposed credentials.
* Unsafe input handling.
* HTML injection.
* Command injection.
* Path traversal.
* Unsafe file or network operations.
* Sensitive information in logs.
* Unnecessary network exposure.

Keep the local server bound to localhost.
Fix issues relevant to this implementation.

## 12. Performance and Dependencies

Check for obvious unnecessary computation, repeated expensive operations,
excessive memory use, and blocking behavior.

Execute the shared checks at startup rather than on every page request.

Prefer simple code over premature optimization.
Use existing dependencies and the standard library wherever possible.

## 13. Documentation

Update `README.md` with:

* What the application does.
* JDK and sbt prerequisites.
* Scala version.
* Dependency download behavior on the first build.
* How to compile.
* How to run tests.
* How to start and stop the application.
* The localhost URL and port.
* How test results are generated and displayed.
* The shared-check design.
* Unknown-path behavior.

Keep documentation accurate and concise.

## 14. Git

* Inspect current Git status before changes.
* Preserve unrelated user changes.
* Do not revert or delete user work.
* Review files changed by this task.
* Verify generated build artifacts are ignored.
* Check for whitespace errors.
* After implementation and verification pass, stage, commit, and push the
  Scala project changes as part of this workflow, unless the user asks otherwise.
* Run the commands below from the parent repository. Inspect the current branch,
  remote, and staged diff first. Stage only files changed by this task; never
  include unrelated user changes, secrets, or generated build artifacts.
* If unrelated changes are already staged, preserve them and isolate this task's
  commit rather than including or unstaging them.

```sh
cd /Users/developer1/WebstormProjects/ivaluemart-marketplace.
git status --short
git branch --show-current
git remote -v
git diff --check
git diff --cached

# For initial project creation; for later changes, list only the affected files.
git add -- scala-hello-world/CLAUDE.md scala-hello-world/README.md scala-hello-world/.gitignore scala-hello-world/build.sbt scala-hello-world/project/build.properties scala-hello-world/src/main/scala/Hello.scala scala-hello-world/src/main/scala/HelloChecks.scala scala-hello-world/src/main/scala/Server.scala scala-hello-world/src/test/scala/HelloTest.scala
git diff --cached --check
git diff --cached
git commit -m "Create Scala Hello World application with shared checks"
git push -u origin HEAD
git status --short
```

* Adapt the commit message to the actual change. Skip committing when there are
  no task changes. Confirm the intended branch and remote before pushing; if
  either is ambiguous, ask the user for the missing destination.
* Never force-push. If a push fails, inspect and report the cause without
  overwriting remote history. Report the commit hash and actual push outcome.

## 15. Final Verification

Before declaring completion, verify:

* [ ] Project exists at the specified location.
* [ ] Source code is complete.
* [ ] Project compiles successfully.
* [ ] Automated tests are present.
* [ ] All relevant tests pass.
* [ ] Application starts successfully.
* [ ] HTTP root and unknown-path behavior are verified.
* [ ] Page displays accurate check results.
* [ ] Errors and resources are handled appropriately.
* [ ] Security and performance have been reviewed.
* [ ] Documentation is accurate.
* [ ] CLAUDE.md contains these instructions.
* [ ] Git changes have been reviewed.
* [ ] Task changes have been staged and committed, or there were no changes.
* [ ] Commit has been pushed to the intended remote branch, or a blocker is reported.

## 16. Final Response

Provide a concise summary containing:

### Implemented

What was created, restored, or fixed.

### Files Changed

Important files created or modified and the full project location.

### Tests

Commands run, test count, and actual results.

### Build

The build command and actual result.

### Run

How to start the application, its URL, and whether it is currently running.

### Issues

Any remaining problems, limitations, or unverified behavior.

### Git

Commit hash, destination branch, and push result, or why a step was skipped
or blocked.

### Next Steps

Include only if useful work remains.

## Important Rules

* Actually inspect and work on the project.
* Actually run the build, tests, and application whenever possible.
* Verify changes after making them.
* Do not claim success without evidence.
* Do not modify unrelated code.
* Do not delete or overwrite user work.
* Do not add unnecessary dependencies.
* Keep changes focused on the requested Scala project.
* Continue until the requested functionality works and is verified,
  or clearly report a concrete blocker.

## Authorized End-to-End Workflow

ACCEPT ALL → INSPECT ALL → IMPLEMENT ALL → BUILD ALL → RUN ALL → TEST ALL →
FIX ALL → RETEST ALL → REVIEW ALL → VERIFY ALL → DOCUMENT ALL → GIT REVIEW →
COMMIT → PUSH → VERIFY FINAL STATE → REPORT ACTUAL RESULTS.

Normal development operations, task-only staging, committing, and pushing to
an unambiguous configured destination are authorized without repeated permission.
Run `sbt compile`, the complete `sbt test` suite, and `sbt run`. On errors,
inspect the actual failure, fix its cause, rebuild, rerun affected tests and the
full suite, and repeat runtime verification. Never disable or weaken tests.
Twelve required shared checks are a minimum, not a limit on ScalaTest cases.

Verify console output, localhost binding, root status/body, individual results,
totals, overall status, UTF-8, byte content lengths, and 404/Not Found responses.
Verify temporary server, stream, and connection cleanup and shutdown behavior.
Escape diagnostics as well as names. Restart to rerun startup checks; do not
rerun them for each request. Leave the application running after verification.

Review staged changes and generated-file ignores; run `git diff --check` and
`git diff --cached --check`. Preserve unrelated staged work. Never force-push.
Record the actual commit hash, branch, remote, and push result. Investigate
concrete environmental blockers and report unresolved requirements honestly.
Do not declare completion before all required verification and the push succeed.
The final report must include implementation, files, build, complete test count,
running status and URL, HTTP results, Git results, and remaining issues.
