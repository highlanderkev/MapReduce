package server

import common._
import com.typesafe.config.ConfigFactory
import akka.actor.{Actor, ActorRef, ActorLogging, ActorSystem, Props, AddressFromURIString}
import akka.remote.routing.RemoteRouterConfig
import akka.routing.RoundRobinPool

object Server extends App {
  val system = ActorSystem("RemoteMapReduceApp", ConfigFactory.load.getConfig("server"))
  println("Server ready")
  val master = system.actorOf(Props[MasterActor], name = "master")
  println(master.path)
}
