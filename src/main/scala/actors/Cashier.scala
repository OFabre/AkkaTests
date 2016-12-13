package actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.event.Logging
import messages._

import scala.util.Random

/**
  * Created by olden on 15/11/16.
  */
class Cashier extends Actor {
  val logger = Logging.getLogger(ActorSystem("helloakka"), "Cashier")
  val system = ActorSystem("helloakka")
  val waiter: ActorRef = system.actorOf(Props[Waiter], "waiter")
  var nbCustomer = 0

  override def receive: Receive = {
    case (Hello) =>
      nbCustomer += 1
      val waitTime: Long = 1000 + Random.nextInt(1000) // TODO : Use global conf to set waiting time
      logger.info(s"I am busy with a client (for $waitTime ms) and now have $nbCustomer customers")
      waiter ! TimingOut(sender, waitTime)
    case (Done(sender)) =>
      sender ! GoodBye
      nbCustomer -= 1
      logger.info(s"Done with $sender, I now have $nbCustomer customers")
    case (HowManyCustomers) =>
      logger.info(s"I have $nbCustomer customers. This is my answer.")
      sender ! ThisManyCustomers(nbCustomer)
  }
}
