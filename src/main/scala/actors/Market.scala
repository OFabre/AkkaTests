package actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import messages._

/**
  * Created by olden on 15/11/16.
  */
case class Market() extends Actor {
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
      println(s"Hello $sender")
      sender ! GoToThisCashier(whichOne)
      println(s"$sender, go there : $whichOne")
      whichOne ! HowManyCustomers
    case (ThisManyCustomers(n)) =>
      cashiers -= sender
      println(s"ok, $sender, you have $n customers")
      cashiers += sender -> n
  }
}
