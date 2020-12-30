package se.mesmeri.mesmerise
import se.mesmeri.mesmerise.props.*
import org.slf4j.*
import java.awt.*
import javax.swing.JFrame

// TODO: let background, score, etc. override any background, score, etc. in scene
class Viewer(mkStage : (Int, Int) -> Stage) : JFrame() {
    private var light = null as Prop?
    private var background = null as Prop?
    private var score = null as Prop?
    private var scene = null as Scene?
    private val logger = LoggerFactory.getLogger(javaClass)
    private val stage : Stage
    init {
        logger.debug("Initializing viewer")
        val dim = Toolkit.getDefaultToolkit().screenSize!!
        this.setSize(dim.width, dim.height)
        this.stage = mkStage(dim.width, dim.height)
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
        this.background?.exitStage(stage)
        this.background = p
        p.enterStage(stage)
        this.repaint()
    }

    fun setScore(p : Prop) {
        if (p != this.score) {
            logger.debug("Setting score to {}", p)
            this.score?.exitStage(stage)
            this.score = p
            p.enterStage(stage)
            this.repaint()
        } else {
            logger.debug("Score {} already playing; ignoring", p)
        }
    }

    fun setLight(p : Prop) {
        logger.debug("Setting lighting to {}", p)
        this.light?.exitStage(stage)
        this.light = p
        p.enterStage(stage)
        this.repaint()
    }

    fun setScene(newScene : Scene) {
        logger.debug("Setting scene to {}", newScene)
        val oldScene = this.scene
        this.scene = newScene
        if (oldScene != null) {
            val exitingProps = diffScenes(newScene, oldScene)
            val enteringProps = diffScenes(oldScene, newScene)
            exitingProps.exitStage(stage)
            enteringProps.enterStage(stage)
        } else {
            newScene.enterStage(stage)
        }
        this.repaint()
    }

    /**
     * Returns a scene containing all props which need to change in order to get from the first scene
     * to the second.
     */
    private fun diffScenes(oldScene: Scene, newScene: Scene): Scene = Scene(
        background = diffProp(oldScene.background, newScene.background),
        score = diffProp(oldScene.score, newScene.score),
        light = diffProp(oldScene.light, newScene.light),
    )
    
    private fun diffProp(oldProp: Prop?, newProp: Prop?): Prop? =
        if (oldProp == newProp) { null } else { newProp }

    fun silence() {
        logger.debug("Killing music")
        this.scene?.score?.exitStage(stage)
        this.score?.exitStage(stage)
    }

    fun unsilence() {
        this.silence()
        logger.debug("Restarting music")
        this.scene?.score?.enterStage(stage)
        this.score?.enterStage(stage)
    }

    override fun paint(g : Graphics) {
        super.paint(g)
        this.scene?.render(stage, g)
        this.background?.render(stage,  g)
        this.score?.render(stage, g)
    }
}