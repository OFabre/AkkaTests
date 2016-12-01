package messages

import akka.actor.ActorRef

/**
  * Created by olden on 29/11/16.
  */
case class Done(waitingActor: ActorRef) {

}
