package actors

import java.time.Duration

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.event.Logging
import akka.pattern.Patterns
import akka.util.Timeout
import com.sun.deploy.util.Waiter
import messages._

import scala.concurrent.duration._
import scala.util.Random

/**
  * Created by olden on 15/11/16.
  */
class Cashier extends Actor {
  val logger = Logging.getLogger(ActorSystem("helloakka"), "Cashier")
  var nbCustomer = 0
  val system = ActorSystem("helloakka")
  val waiter: ActorRef = system.actorOf(Props[Waiter], "waiter")

  override def receive: Receive = {
    case (Hello) =>
      nbCustomer += 1
      val waitTime: Long = Random.nextInt(5000)
      logger.info(s"I am busy with a client (for $waitTime ms) and now have $nbCustomer customers")
      waiter ! TimingOut(sender, waitTime)
    case(Done(sender)) =>
      sender ! GoodBye
      nbCustomer -= 1
      logger.info(s"Done with $sender, I now have $nbCustomer customers")
    case (HowManyCustomers) =>
      ThisManyCustomers(nbCustomer)
  }
}
