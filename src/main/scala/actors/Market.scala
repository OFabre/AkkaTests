package actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.event.Logging
import messages._

/**
  * Created by olden on 15/11/16.
  */
case class Market() extends Actor {
  val logger = Logging.getLogger(ActorSystem("helloakka"), "Market")

  val cashiers: scala.collection.mutable.Map[ActorRef, Int] = scala.collection.mutable.Map[ActorRef, Int](
    ActorSystem("helloakka").actorOf(Props[Cashier]) -> 0,
    ActorSystem("helloakka").actorOf(Props[Cashier]) -> 0,
    ActorSystem("helloakka").actorOf(Props[Cashier]) -> 0,
    ActorSystem("helloakka").actorOf(Props[Cashier]) -> 0
  )

  def whichOne: ActorRef = {
    cashiers.toList.sortBy(_._2).head._1
  }

  override def receive: Receive = {
    case (Hello) =>
      logger.info(s"Hello $sender")
      val choice = whichOne
      sender ! GoToThisCashier(choice)
      logger.info(s"$sender, go there : $choice")
      choice ! HowManyCustomers
    case (ThisManyCustomers(n)) =>
      cashiers -= sender
      logger.info(s"ok, $sender, you have $n customers")
      cashiers += sender -> n
  }
}
