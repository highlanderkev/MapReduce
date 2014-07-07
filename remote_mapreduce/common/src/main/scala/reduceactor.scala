package common

import scala.collection.mutable.HashMap
import scala.collection.immutable.List
import akka.actor.{Actor, ActorRef}
import com.typesafe.config.ConfigFactory

class ReduceActor extends Actor {
  //var remainingMappers = ConfigFactory.load.getInt("number-mappers")
  var remainingMappers = 4
  var reduceMap = HashMap[String, List[String]]()
  
  def receive = {
    case REDUCE(name: String, title: String) =>
      if(reduceMap.contains(name)){
        reduceMap += (name -> (title :: reduceMap(name)))
      }
      else{
        reduceMap += (name -> (title :: Nil))
      }
    case Flush =>
      remainingMappers -= 1
      if(remainingMappers == 0){
        println(self.path.toStringWithoutAddress + " : " + reduceMap)
        context.parent ! Done
      }
  }
}
