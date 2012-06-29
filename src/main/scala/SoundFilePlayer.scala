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
 * 
 * TODO This doesn't need to extend BasicPlayerListener.
 * 
 * Sample use:
 * 
 * val player = new SoundFilePlayer(filename)
 * player.play
 * 
 */
class SoundFilePlayer(soundFileName: String) {

  private var basicPlayer = new BasicPlayer
  private val basicPlayerController:BasicController = basicPlayer.asInstanceOf[BasicController]  

  val DEFAULT_GAIN = 0.5

  // java audio
  var clip:Clip = _
  
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
  @throws(classOf[Exception])
  def play {
    if (soundFileName.toLowerCase().endsWith(".mp3")) {
      playMp3WithJLayer
    } else {
      playSoundFileWithJavaAudio
    }
  }

  /**
   * Call this method to properly close everything.
   */
  def close {
    if (soundFileName.endsWith(".mp3")) {
      if (basicPlayer != null)
        try {
          basicPlayerController.stop
        } catch {
          case e: BasicPlayerException => // TODO 
        }
    } else {
      if (clip != null)
        clip.close
    }
  }

  // from
  // http://www.java2s.com/Code/Java/Development-Class/AnexampleofloadingandplayingasoundusingaClip.htm
  @throws(classOf[UnsupportedAudioFileException])
  @throws(classOf[IOException])
  @throws(classOf[LineUnavailableException])
  def playSoundFileWithJavaAudio {
    val sound = AudioSystem.getAudioInputStream(new File(soundFileName))

    // load the sound into memory (a Clip)
    // .asInstanceOf[BasicController]
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

  //---------------------- JLayer Experiments ------------------------
  
  @throws(classOf[BasicPlayerException])
  private def playMp3WithJLayer {
    basicPlayerController.open(new File(soundFileName))
    basicPlayerController.play
    basicPlayerController.setGain(DEFAULT_GAIN)
//     // Set Pan (-1.0 to 1.0).
//     control.setPan(0.0);
  }

  /**
   * only works for jlayer mp3 player
   */
  @throws(classOf[BasicPlayerException])
  def pause {
    basicPlayerController.pause
  }
  
  /**
   * only works for jlayer mp3 player
   */
  @throws(classOf[BasicPlayerException])
  def stop {
    basicPlayerController.stop
  }

  /**
   * only works for jlayer mp3 player
   */
  @throws(classOf[BasicPlayerException])
  def resume {
    basicPlayerController.resume
  }

  /**
   * @param volume
   * A number between 0.0 and 1.0 (loudest)
   * @throws BasicPlayerException
   */
  @throws(classOf[BasicPlayerException])
  def setGain(volume: Double) {
    // TODO i'm now ignoring the java code that plays wav files, and
    // just implementing code for the mp3 player
    basicPlayerController.setGain(volume)
  }

}











