package com.alvinalexander.sound

import java.io.File
import java.io.IOException
import java.util.Map
import javax.sound.sampled._
import javazoom.jlgui.basicplayer.BasicController
import javazoom.jlgui.basicplayer.BasicPlayer
import javazoom.jlgui.basicplayer.BasicPlayerEvent
import javazoom.jlgui.basicplayer.BasicPlayerException
import javazoom.jlgui.basicplayer.BasicPlayerListener
import scala.collection.JavaConversions._

/**
 * Call our factory method to get the proper player.
 * ".mp3" files return the Mp3SoundFilePlayer, other filenames
 * get the JavaSoundFilePlayer.
 */
object SoundFilePlayer {
  /**
   * Our simple factory method.
   */
  def getSoundFilePlayer(filename: String):BaseSoundFilePlayer = {
    if (filename.toLowerCase.contains(".mp3")) {
      new Mp3SoundFilePlayer(filename)
    } else {
      new JavaSoundFilePlayer(filename)
    }
  }
}

/**
 * This is the basic trait for all sound file players.
 */
trait BaseSoundFilePlayer {

  /**
   * Calling play() on JavaSound files (wav, aiff) blocks automatically.
   * 
   * Calling play() with MP3 files does not block automatically, and you
   * have to use a BasicSoundListener to listen for the EOM event.
   */
  def play
  
  /**
   * Both players will attempt to properly close their resources.
   */
  def close
  
  /**
   * JavaSoundFilePlayer does not implement this.
   */
  def pause {}
  
  /**
   * JavaSoundFilePlayer does not implement this.
   */
  def stop {}
  
  /**
   * JavaSoundFilePlayer does not implement this.
   */
  def resume {}
}

/**
 * This trait shows the additional methods that are available on
 * an Mp3SoundFilePlayer.
 */
trait Mp3BaseSoundFilePlayer extends BaseSoundFilePlayer {
  def getBasicPlayer:BasicPlayer
  def getBasicController:BasicController
  def setGain(volume: Double)
}

/**
 * A JavaSound implementation of a SoundPlayer.
 * TODO - How do I make it so others can't create this class themselves?
 */
class JavaSoundFilePlayer (soundFileName: String) extends BaseSoundFilePlayer {

  // javasound uses a clip
  var clip:Clip = _
  
  /**
   * Call this method to play the file.
   * @throws Exception
   */
  @throws(classOf[Exception])
  override def play {
    playSoundFileWithJavaAudio
  }

  // from
  // http://www.java2s.com/Code/Java/Development-Class/AnexampleofloadingandplayingasoundusingaClip.htm
  @throws(classOf[UnsupportedAudioFileException])
  @throws(classOf[IOException])
  @throws(classOf[LineUnavailableException])
  def playSoundFileWithJavaAudio {
    val sound = AudioSystem.getAudioInputStream(new File(soundFileName))
    // load the sound into memory (a Clip)
    val info = new DataLine.Info(classOf[Clip], sound.getFormat)  // DataLine.Info
    val clip = AudioSystem.getLine(info).asInstanceOf[Clip]
    clip.open(sound)

    // due to bug in Java Sound, explicitly exit the VM when
    // the sound has stopped.
    clip.addLineListener(new LineListener() {
      def update(event: LineEvent) {
        if (event.getType == LineEvent.Type.STOP) {
          event.getLine.close
        }
      }
    })

    // play the sound clip
    clip.start
  }

  /**
   * Call this method to properly close everything.
   */
  override def close { if (clip != null) clip.close }

}



/**
 * An MP3 player implementation of a SoundPlayer.
 */
class Mp3SoundFilePlayer (soundFileName: String) extends Mp3BaseSoundFilePlayer {

  private var basicPlayer = new BasicPlayer
  private val basicPlayerController:BasicController = basicPlayer.asInstanceOf[BasicController]  

  val DEFAULT_GAIN = 0.5

  /**
   * Access to our BasicPlayer reference.
   */
  def getBasicPlayer:BasicPlayer = basicPlayer

  /**
   * Access to our BasicController reference.
   */
  def getBasicController:BasicController = basicPlayerController

  /**
   * Specify both the filename and a listener.
   */
  def this(soundFileName: String, basicPlayerListener: BasicPlayerListener) {
    this(soundFileName)
    basicPlayer.addBasicPlayerListener(basicPlayerListener)
  }

  /**
   * Call this method to play the file.
   * @throws Exception
   */
  //@throws(classOf[Exception])
  @throws(classOf[BasicPlayerException])
  def play {
    basicPlayerController.open(new File(soundFileName))
    basicPlayerController.play
    basicPlayerController.setGain(DEFAULT_GAIN)
//     // Set Pan (-1.0 to 1.0).
//     control.setPan(0.0);
  }

  /**
   * Call this method to properly close everything.
   */
  def close {
    if (basicPlayer != null) {
      try {
        basicPlayerController.stop
      } catch {
        case e: BasicPlayerException => // TODO 
      }
    }
  }

  @throws(classOf[BasicPlayerException])
  override def pause {
    basicPlayerController.pause
  }
  
  @throws(classOf[BasicPlayerException])
  override def stop {
    basicPlayerController.stop
  }

  @throws(classOf[BasicPlayerException])
  override def resume {
    basicPlayerController.resume
  }

  /**
   * @param volume
   * A number between 0.0 and 1.0 (loudest)
   * @throws BasicPlayerException
   */
  @throws(classOf[BasicPlayerException])
  def setGain(volume: Double) {
    basicPlayerController.setGain(volume)
  }

}











