package actors

import java.util.logging.Logger

import akka.actor.{Actor, ActorRef, ActorSystem}
import akka.event.Logging
import messages.{Done, TimingOut}
import org.slf4j.LoggerFactory

import scala.collection.mutable

/**
  * Created by olden on 29/11/16.
  */
class Waiter extends Actor {
  val logger = Logging.getLogger(ActorSystem("helloakka"), "Waiter")

  override def receive: Receive = {
    case (TimingOut(waitingActor, ms)) =>
      Thread.sleep(ms)
      sender ! Done(waitingActor)
  }
}
