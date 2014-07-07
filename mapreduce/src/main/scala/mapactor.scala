package mapreduce

import scala.collection.mutable.HashMap
import scala.io.Source
import akka.actor.{Actor, ActorRef}

class MapActor(reduceActors: List[ActorRef]) extends Actor{
  
  val STOP_WORDS_LIST = List("a", "am", "an", "and", "are", "as", "at", "be", "do", "go", "if", "in", "is", "it", "of", "on", "the", "to")
  val numReducers = reduceActors.size
  val myHashMap = HashMap[String, String]()

  def receive = {
    case MAP(title: String, url: String) =>
      process(title, url)
    case Flush =>
      for(i <- 0 until numReducers){
        reduceActors(i) ! Flush
      }
  }

  def process(title: String, url: String) = {
    var content = Source.fromURL(url).mkString
    for(name <- content.split("[\\p{Punct}\\s]+")){
      if((!STOP_WORDS_LIST.contains(name.toLowerCase)) && (name.matches("([A-Z][a-z]*\\s*)+"))){
        myHashMap += (name -> url)
      }
    }
    myHashMap.keys.foreach{ name => 
      var index = Math.abs((name.hashCode())%numReducers)
      reduceActors(index) ! REDUCE(name, title)
    }
  }
}
