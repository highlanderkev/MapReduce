package server

import common._
import akka.actor.{Actor, ActorRef, Props}
import akka.remote.routing.RemoteRouterConfig
import akka.routing.{Broadcast, RoundRobinPool}
import com.typesafe.config.ConfigFactory

class MasterActor extends Actor {
  
  var numberMappers = ConfigFactory.load.getInt("number-mappers")
  var numberReducers = ConfigFactory.load.getInt("number-reducers")
  var pending = numberReducers

  //Setup local Reducers
  var reduceActors = List[ActorRef]()
  for(i <- 0 until numberReducers){
    reduceActors = context.actorOf(Props[ReduceActor], name="localreduce"+i)::reduceActors
    println(reduceActors.toString)
  }

  //Setup MapActors
  var mapActors = List[ActorRef]()
  def setMapActors(reducers: List[ActorRef]) : Unit = {
    //Setup local Mappers
    for(i <- 0 until numberMappers){
      mapActors = context.actorOf(Props(classOf[MapActor], reducers), name="localmap"+i)::mapActors
      println(mapActors.toString)
    }
  }
  
  var actorRouter = context.actorOf(Props.empty)
  var clients = List[ActorRef]()

  def receive = {
    case AddRemoteMap(actors: List[ActorRef]) =>
      for(actor <- actors){
        mapActors = actor :: mapActors
        numberMappers = numberMappers + 1
      }
      var addresses = mapActors.map(e => e.path.address).seq
      actorRouter = context.actorOf(RemoteRouterConfig(RoundRobinPool(numberMappers), addresses).props(Props(classOf[MapActor], reduceActors)))
      sender ! START
    case AddRemoteReduce(actors: List[ActorRef], client:ActorRef) =>
      for(actor <- actors){
        reduceActors = actor :: reduceActors
      }
      pending = pending + actors.size
      println("Jobs pending: " + pending)
      setMapActors(reduceActors)
      println("New Client connected: " + client.path.toString)
      clients = client :: clients
      sender ! AddRemoteMap(reduceActors)
    case MAP(title: String, url: String) =>
      actorRouter ! MAP(title, url)
    case Flush =>
      actorRouter ! Broadcast(Flush)
    case Done =>
      pending -= 1
      println("Job done, jobs pending: " + pending)
      if(pending == 0){
        for(client <- clients){
          println("Sending shutdown to client: " + client.path.toString)
          client ! SHUTDOWN
        }
        println("Shutting down server.")
        Thread.sleep(20000)
        context.system.shutdown
      }
  }
}
