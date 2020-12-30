package se.mesmeri.mesmerise.props
import javazoom.jl.player.Player
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Files
import kotlin.concurrent.thread

class Song(path : String) : Prop {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val file = File(path)
    private var player = null as Player?
    private var playerJob = null as Thread?
    private var playing = false

    // TODO: queue sounds on dedicated audio thread instead
    override fun enterStage(stage : Stage) {
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

    override fun exitStage(stage : Stage) {
        this.playing = false
        this.player?.close()
        this.playerJob?.join()
        this.playerJob = null
    }

    private fun loadFile(file : File) : Player {
        logger.info("Loading MP3: {}", file.absolutePath)
        // TODO: use java.nio instead, also .use() to fix resource leak
        return Player(Files.newInputStream(file.toPath()).buffered())
    }
}