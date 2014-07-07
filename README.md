MapReduce
===

| README For MapReduce |
---
| CSC 536: Distributed Systems 2 |
| Professor Ljubomir Perkovic |
| By Student Kevin Westropp |


There are two main directories:

1. mapreduce - this contains scala/sbt code for Local Map Reduce

2. remote_mapreduce - this contains scala/sbt code for Map Reduce with Remote Actors

To Compile
---
1. navigate to root folder /mapreduce
           
           $ sbt compile

2. start two shells/terminals and navigate to /remote_mapreduce/server & /remote_mapreduce/client

           $ sbt compile

To Run
---
1. folder /mapreduce

           $ sbt run

2. folder /remote_mapreduce/server
           $ sbt run

then folder /remote_mapreduce/client

           $ sbt run

Stopping
---
1. should automatically shutdown after map reduce jobs finish. 
        
           $ (Ctrl-x Ctrl-C) if needed

2. also will auto shutdown (with a delay) after map reduce jobs finish.

           $ (Ctrl-x Ctrl-C) if needed 
