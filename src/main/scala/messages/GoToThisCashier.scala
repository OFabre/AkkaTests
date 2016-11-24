package messages

import actors.Cashier
import akka.actor.ActorRef

/**
  * Created by olden on 23/11/16.
  */
case class GoToThisCashier(cashierRef: ActorRef)