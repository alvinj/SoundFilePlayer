package foo

import com.alvinalexander.sound._
import javazoom.jlgui.basicplayer._
import scala.collection.JavaConversions._
// this was necessary to get past compiler problems
import java.util.Map

object TestMp3 extends App {
  
  // the listener only works for mp3 files
  val testClip = "/Users/al/Sarah/plugins/DDRandomNoise/Sounds/yoda-help_you_i_can.mp3" 
  val bpl = new MyListener
  //val playa = new SoundFilePlayer(testClip)
  val playa = SoundFilePlayer.getSoundFilePlayer(testClip).asInstanceOf[Mp3SoundFilePlayer]
  playa.getBasicPlayer.addBasicPlayerListener(bpl)
  
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
  
  Thread.sleep(8000)

}

class MyListener extends BasicPlayerListener {
  def opened(stream: Any, properties:Map[_, _] ) { println("opened() called") }
  def progress(bytesread: Int, microseconds: Long, pcmdata: Array[Byte], properties: Map[_, _]) {}
  def setController(controller: BasicController) {}
  def stateUpdated(event: BasicPlayerEvent) {
    println("stateUpdated() called, time = " + System.currentTimeMillis)
    println("stateUpdated() event.getCode = " + event.getCode)
    event.getCode match {
      case BasicPlayerEvent.EOM     => println("clip ended (eom)")
      case BasicPlayerEvent.OPENING => println("clip opening")
      case BasicPlayerEvent.PLAYING => println("clip playing")
      case BasicPlayerEvent.STOPPED => println("clip stopped")
      case BasicPlayerEvent.PAUSED  => println("clip paused")
      case BasicPlayerEvent.UNKNOWN => println("unknown")
    }
  }
}

    
    
    
    
    