package client

import common._
import com.typesafe.config.ConfigFactory
import akka.actor.{Actor, ActorRef, ActorLogging, ActorSystem, Props, AddressFromURIString}
import akka.remote.routing.RemoteRouterConfig
import akka.routing.RoundRobinPool

object Client extends App {
  val system = ActorSystem("RemoteMapReduceApp", ConfigFactory.load.getConfig("remotelookup"))
  println("Client Ready")
  val client = system.actorOf(Props[ClientActor], name="client")
  println(client.path)
}
