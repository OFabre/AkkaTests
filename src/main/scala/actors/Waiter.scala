package actors

import akka.actor.{Actor, ActorSystem}
import akka.event.Logging
import messages.{Done, TimingOut}

/**
  * Created by olden on 29/11/16.
  */
class Waiter extends Actor {
  val logger = Logging.getLogger(ActorSystem("helloakka"), "Waiter")

  override def receive: Receive = {
    // TODO : respond directly to waiting actor ?
    case (TimingOut(waitingActor, ms)) =>
      Thread.sleep(ms)
      sender ! Done(waitingActor)
  }
}
