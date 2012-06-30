package foo

import com.alvinalexander.sound._
import javazoom.jlgui.basicplayer._
import scala.collection.JavaConversions._
// this was necessary to get past compiler problems
import java.util.Map

object TestJavaSound extends App {
  
  val testClip = "/Users/al/Sarah/plugins/DDRandomNoise/HAL9000/this-mission-too-important.wav" 
  val playa = SoundFilePlayer.getSoundFilePlayer(testClip)
  
  try {
    val start = System.currentTimeMillis
    println("just before play: " + start)
    playa.play
    val stop = System.currentTimeMillis
    println("just after play: " + stop)
    println("diff: " + (stop-start))
  } catch {
    case e:Exception => println(e.getMessage)
  }
  
}


