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
    logger.info("Looking for good cashier inside : " + cashiers)
    val sorted: List[(ActorRef, Int)] = cashiers.toList.sortBy(_._2)
    logger.info("When sorted : " + sorted)
    val choice = sorted.head._1
    logger.info("The less busy one is : " + choice)
    choice
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
    case (AreYouAllDone) =>
      logger.info("Getting asked if I am done. Sending global request.")
      cashiers.foreach(_._1 ! HowManyCustomers)
      val done: Boolean = cashiers.forall(_._2 == 0)
      if(done) logger.info("Yes I am")
      else logger.info("No I am not : " + cashiers)
      sender ! AmIDone(done)
  }
}
