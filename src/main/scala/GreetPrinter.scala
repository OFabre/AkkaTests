import akka.actor.Actor

/**
  * Created by olden on 15/11/16.
  */
// prints a greeting
class GreetPrinter extends Actor {
  def receive = {
    case Greeting(message) => println(message)
  }
}
