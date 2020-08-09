package io.bugsbunny.storageProviders

abstract class Buffer[+T]
{
  val element: T
}

abstract class SeqBuffer[U, +T <: Seq[U]] extends Buffer[T]
{
  def length = element.length
}

object LogBuffer extends SeqBuffer[Int, Seq[Int]]
{
  val element = List(7, 8)
}

object HelloChukyChuky extends App
{
  println("Hello, World!")

  /*def newIntSeqBuf(e1: Int, e2: Int): SeqBuffer[Int, Seq[Int]] = new SeqBuffer[Int, List[Int]]
  {
    val element = List(e1, e2)
  }*/

  /*val buf = LogBuffer(7,8)
  println("length = " + buf.length)
  println("content = " + buf.element)*/
}
