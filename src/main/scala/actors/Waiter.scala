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
  val q = new mutable.Queue[(ActorRef, ActorRef, Long)]

  new Thread(new Runnable {
    override def run(): Unit = {
      while (true) {
        logger.debug("Looking queue...")
        if (q.isEmpty) {
          logger.debug("Nothing")
          Thread.sleep(1000)
        }
        else {
          logger.debug("New item")
          val (sender, a, t) = q.dequeue()
          Thread.sleep(t)
          sender ! Done(a)
        }
      }
    }
  }).start()

  override def receive: Receive = {
    case (TimingOut(waitingActor, ms)) => q.enqueue((sender, waitingActor, ms))
  }
}
