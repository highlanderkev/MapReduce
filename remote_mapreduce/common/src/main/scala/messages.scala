package common

import akka.actor.{Actor, ActorRef}

case class MAP(title: String, url: String)
case class REDUCE(name: String, title: String)
case class AddRemoteMap(actors: List[ActorRef])
case class AddRemoteReduce(actors: List[ActorRef], client:ActorRef)
case object START
case object SHUTDOWN
case object Flush
case object Done

