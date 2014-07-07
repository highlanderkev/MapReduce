package client

import common._
import com.typesafe.config.ConfigFactory
import akka.actor.{Actor, ActorRef, ActorLogging, ActorSystem, Props, AddressFromURIString}
import akka.remote.routing.RemoteRouterConfig
import akka.routing.RoundRobinPool

class ClientActor extends Actor{
  
  var numberMappers = ConfigFactory.load.getInt("number-mappers")
  var numberReducers = ConfigFactory.load.getInt("number-reducers")

  //Setup remote Reducers
  var reduceActors = List[ActorRef]()
  for(i <- 0 until numberReducers){
    reduceActors = context.actorOf(Props[ReduceActor], name="remotereduce"+i)::reduceActors
    println(reduceActors.toString)
  }
  
  //Get Master Actor
  val master = context.actorSelection("akka.tcp://RemoteMapReduceApp@127.0.0.1:2552/user/master")

  //Send list of Reduce Actors and self to master
  master ! AddRemoteReduce(reduceActors, self)
  println("Client has sent the reducers to the master actor ...")
  
  //Setup MapActors
  var mapActors = List[ActorRef]()
  def setMapActors(reducers: List[ActorRef]): Unit = {
    //Setup remote Mappers
    for(i <- 0 until numberMappers){
      mapActors = context.actorOf(Props(classOf[MapActor], reducers), name="remotemapper"+i)::mapActors
      println(mapActors.toString)
    }
  }

  def receive = {
    case AddRemoteMap(reducers:List[ActorRef]) =>
      setMapActors(reducers)
      sender ! AddRemoteMap(mapActors)
      println("Client has sent the mappers to the master actor...")
    case Flush =>
      println("Flush received from server.")
    case Done => 
      println(sender + " map reduce done, forwarding to server.")
      master ! Done
    case START =>
      master ! MAP("Bleak House" ,"http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg1023.txt")
      master ! MAP("Great Expectations" ,"http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg1400.txt")
      master ! MAP("A Christmas Carol" ,"http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg19337.txt")
      master ! MAP("The Cricket on the Hearth" ,"http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg20795.txt")
      master ! MAP("The Pickwick Papers" ,"http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg580.txt")
      master ! MAP("A Child's History of England" ,"http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg699.txt")
      master ! MAP("The Old Curiosity Shop" ,"http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg700.txt")
      master ! Flush
    case SHUTDOWN =>
      println("Client received shutdown, shutting down client.")
      Thread.sleep(10000)
      context.system.shutdown
    case _ =>
      println("Client received unknown msg.")
  }
}

