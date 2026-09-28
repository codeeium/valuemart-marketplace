import org.scalatest.funsuite.AnyFunSuite

class HelloTest extends AnyFunSuite {
  HelloChecks.checks.foreach { check =>
    test(check.name) { check.verify() }
  }

  test("Failed checks retain diagnostics and safely render them") {
    val result = HelloChecks.Check("Broken check", () =>
      throw new IllegalStateException("<bad> & café")
    ).run()
    assert(!result.passed)
    assert(result.name == "Broken check")
    assert(result.diagnostic.contains("IllegalStateException: <bad> & café"))
    val page = Server.page(Seq(result))
    assert(page.contains("FAILING"))
    assert(page.contains("IllegalStateException: &lt;bad&gt; &amp; café"))
    assert(!page.contains("<bad>"))
  }
}
