package cc.ekblad.mesmerise
import javazoom.jl.player.Player
import org.slf4j.LoggerFactory
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import kotlin.concurrent.thread

class Song(path : String) : Prop {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val file = File(path)
    private var player = null as Player?
    private var playerJob = null as Thread?
    private var playing = false

    override fun enterStage(screenW : Int, screenH : Int) {
        logger.debug("Launching player job for {}", file.absolutePath)
        this.playing = true
        this.playerJob = thread(isDaemon = true) {
            logger.debug("Playing {}", file.absolutePath)
            try {
                while (playing) {
                    this.player = loadFile(file)
                    player?.play()
                }
            } finally {
                logger.debug("Finished playing {}", file.absolutePath)
            }
        }
    }

    override fun exitStage() {
        this.playing = false
        this.player?.close()
        this.playerJob?.join()
        this.playerJob = null
    }

    private fun loadFile(file : File) : Player {
        logger.info("Loading MP3: {}", file.absolutePath)
        return Player(Files.newInputStream(file.toPath()).buffered())
    }
}