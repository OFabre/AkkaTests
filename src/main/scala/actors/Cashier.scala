package actors

import akka.actor.{Actor, ActorSystem}
import messages.{GoodBye, Hello, HowManyCustomers, ThisManyCustomers}

import scala.util.Random

/**
  * Created by olden on 15/11/16.
  */
class Cashier extends Actor {
  var nbCustomer = 0

  override def receive: Receive = {
    case (Hello) =>
      nbCustomer += 1
      println(s"I am busy with a client and now have $nbCustomer customers")
      sender ! GoodBye
      nbCustomer -= 1
      println(s"Done with $sender, I now have $nbCustomer customers")
    case (HowManyCustomers) =>
      ThisManyCustomers(nbCustomer)
  }
}
