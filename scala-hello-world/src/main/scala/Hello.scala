object Hello:
  def helloWorld(): String = "Hello World"

  def printGreeting(out: java.io.PrintStream): Unit =
    out.println(helloWorld())
