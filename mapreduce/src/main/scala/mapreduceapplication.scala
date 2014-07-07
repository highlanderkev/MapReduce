package mapreduce

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import scala.io.Source

object MapReduceApplication extends App {
  
  val system = ActorSystem("MapReduceApp")
  val master = system.actorOf(Props[MasterActor], name = "master")
 
  master ! MAP("Bleak House" ,"http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg1023.txt") 
  master ! MAP("Great Expectations" ,"http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg1400.txt")
  master ! MAP("A Christmas Carol" ,"http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg19337.txt")
  master ! MAP("The Cricket on the Hearth" ,"http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg20795.txt")
  master ! MAP("The Pickwick Papers" ,"http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg580.txt")
  master ! MAP("A Child's History of England" ,"http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg699.txt")
  master ! MAP("The Old Curiosity Shop" ,"http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg700.txt")
  master ! Flush
}
