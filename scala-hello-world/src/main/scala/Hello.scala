import java.io.PrintStream

object Hello {
  def helloWorld(): String = "Hello World"
  def printGreeting(out: PrintStream): Unit = out.println(helloWorld())
  def main(args: Array[String]): Unit = printGreeting(System.out)
}
