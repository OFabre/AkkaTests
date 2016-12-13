import actors.{Customer, Market}
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.event.Logging
import akka.pattern.ask
import akka.util.Timeout
import messages.{AmIDone, AreYouAllDone}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.postfixOps

case class DoneChecker(market: ActorRef) extends Actor {
  val logger = Logging.getLogger(ActorSystem("helloakka"), "DoneChecker")

  var done = false

  logger.info("Waiting for market to finish")

  market ! AreYouAllDone

  override def receive: Receive = {
    case (AmIDone(true)) => done = true
    case (AmIDone(false)) =>
      Thread.sleep(100)
      market ! AreYouAllDone
    case (AreYouAllDone) => sender ! AmIDone(done)
  }

}

object HelloAkkaScala extends App {
  val logger = Logging.getLogger(ActorSystem("helloakka"), "HelloAkkaScala")

  val system = ActorSystem("helloakka")

  val market = system.actorOf(Props[Market], "market")

  // TODO : record total time of execution

  (1 to args(0).toInt).foreach({ i =>
    system.actorOf(Props(new Customer(market))) // Adds a customer to the market
    Thread.sleep(100)
  })
  implicit val timeout = Timeout(5 seconds)
  var done = false

  while (!done) {
    logger.info("Waiting in vain...")
    Thread.sleep(5000)
    val future: Future[Any] = market ? AreYouAllDone
    Await.result(future, 5 seconds) match {
      case (AmIDone(b)) =>
        logger.info(s"Done : $b")
        done = b
    }
  }

  logger.info("Shuting down the system ...")
  Await.result(system.terminate(), 5 seconds)
  logger.info("System shut down. Exiting")

  System.exit(0)
  /*

  // Create the 'helloakka' actor system
  val system = ActorSystem("helloakka")

  // Create the 'greeter' actor
  val greeter = system.actorOf(Props[Greeter], "greeter")

  // Create an "actor-in-a-box"
  val inbox = Inbox.create(system)

  // Tell the 'greeter' to change its 'greeting' message
  greeter.tell(WhoToGreet("akka"), ActorRef.noSender)

  // Ask the 'greeter for the latest 'greeting'
  // Reply should go to the "actor-in-a-box"
  inbox.send(greeter, Greet)

  // Wait 5 seconds for the reply with the 'greeting' message
  val Greeting(message1) = inbox.receive(5.seconds)
  println(s"Greeting: $message1")

  // Change the greeting and ask for it again
  greeter.tell(WhoToGreet("typesafe"), ActorRef.noSender)
  inbox.send(greeter, Greet)
  val Greeting(message2) = inbox.receive(5.seconds)
  println(s"Greeting: $message2")

  val greetPrinter = system.actorOf(Props[GreetPrinter])
  // after zero seconds, send a Greet message every second to the greeter with a sender of the greetPrinter
  system.scheduler.schedule(0.seconds, 1.second, greeter, Greet)(system.dispatcher, greetPrinter)
  */
}