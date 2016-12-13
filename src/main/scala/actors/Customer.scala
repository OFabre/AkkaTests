package actors

import akka.actor.{Actor, ActorRef, ActorSystem}
import akka.event.Logging
import com.sun.org.apache.bcel.internal.generic.GOTO
import messages.{GoToThisCashier, GoodBye, Hello}

/**
  * Created by olden on 15/11/16.
  */
class Customer(market: ActorRef) extends Actor {
  val logger = Logging.getLogger(ActorSystem("helloakka"), "Customer")

  market ! Hello

  override def receive: Receive = {
    case (GoToThisCashier(cashierRef)) => cashierRef ! Hello
    //case (GoodBye) => logger.info(s"I am done, thanks $sender")
  }
}
