import org.scalatest.funsuite.AnyFunSuite

class HelloTest extends AnyFunSuite {
  HelloChecks.checks.foreach { check =>
    test(check.name) { check.verify() }
  }
}
