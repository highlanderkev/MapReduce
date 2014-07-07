package mapreduce

case class MAP(title: String, url: String)
case class REDUCE(name: String, title: String)
case object Flush
case object Done
