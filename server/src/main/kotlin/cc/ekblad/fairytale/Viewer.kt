package cc.ekblad.fairytale
import org.slf4j.*
import java.awt.*
import javax.swing.JFrame

// TODO: let background, score, etc. override any background, score, etc. in scene
class Viewer() : JFrame() {
    private var background = null as Prop?
    private var score = null as Prop?
    private var scene = null as Scene?
    private val logger = LoggerFactory.getLogger(javaClass)
    init {
        logger.debug("Initializing viewer")
        val dim = Toolkit.getDefaultToolkit().screenSize!!
        this.setSize(dim.width, dim.height)
        this.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        this.extendedState = Frame.MAXIMIZED_BOTH
        this.isUndecorated = true
        this.layout = null
        this.contentPane.background = Color.BLACK
    }

    fun start() {
        logger.debug("Starting viewer")
        this.isVisible = true
        this.repaint()
    }

    fun setBackground(p : Prop) {
        logger.debug("Setting background to {}", p)
        this.background?.exitStage()
        this.background = p
        p.enterStage(this.bounds.width, this.bounds.height)
        this.repaint()
    }

    fun setScore(p : Prop) {
        logger.debug("Setting score to {}", p)
        this.score?.exitStage()
        this.score = p
        p.enterStage(this.bounds.width, this.bounds.height)
        this.repaint()
    }

    fun setScene(s : Scene) {
        logger.debug("Setting scene to {}", s)
        this.scene?.exitStage()
        this.scene = s
        s.enterStage(this.bounds.width, this.bounds.height)
        this.repaint()
    }

    fun silence() {
        logger.debug("Killing music")
        this.scene?.score?.exitStage()
        this.score?.exitStage()
    }

    fun unsilence() {
        this.silence()
        logger.debug("Restarting music")
        this.scene?.score?.enterStage(this.bounds.width, this.bounds.height)
        this.score?.enterStage(this.bounds.width, this.bounds.height)
    }

    override fun paint(g : Graphics) {
        super.paint(g)
        this.scene?.render(g)
        this.background?.render(g)
        this.score?.render(g)
    }
}