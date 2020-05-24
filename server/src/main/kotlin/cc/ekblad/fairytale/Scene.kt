package cc.ekblad.fairytale
import java.awt.Graphics

class Scene(val background : Prop?, val score : Prop?) : Prop {
    override fun enterStage(screenW: Int, screenH: Int) {
        background?.enterStage(screenW, screenH)
        score?.enterStage(screenW, screenH)
    }

    override fun exitStage() {
        background?.exitStage()
        score?.exitStage()
    }

    override fun transitionIn(progress: Double) {
        background?.transitionIn(progress)
        score?.transitionIn(progress)
    }

    override fun transitionOut(progress: Double) {
        background?.transitionOut(progress)
        score?.transitionOut(progress)
    }

    override fun render(g: Graphics) {
        background?.render(g)
        score?.render(g)
    }
}