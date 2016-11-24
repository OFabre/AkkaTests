package actors

import akka.actor.{Actor, ActorRef}
import com.sun.org.apache.bcel.internal.generic.GOTO
import messages.{GoToThisCashier, Hello}

/**
  * Created by olden on 15/11/16.
  */
class Customer(market: ActorRef) extends Actor {
  market ! Hello

  override def receive: Receive = {
    case(GoToThisCashier(cashierRef)) => cashierRef ! Hello
  }
}
