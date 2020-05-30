package se.mesmeri.mesmerise.props
import java.awt.Graphics

class Scene(
    val background : Prop?,
    val score : Prop?,
    val light : Prop?
) : Prop {
    override fun enterStage(stage : Stage) {
        background?.enterStage(stage)
        score?.enterStage(stage)
        light?.enterStage(stage)
    }

    override fun exitStage(stage : Stage) {
        background?.exitStage(stage)
        score?.exitStage(stage)
        light?.exitStage(stage)
    }

    override fun transitionIn(stage : Stage, progress : Double) {
        background?.transitionIn(stage, progress)
        score?.transitionIn(stage, progress)
        light?.transitionIn(stage, progress)
    }

    override fun transitionOut(stage : Stage, progress : Double) {
        background?.transitionOut(stage, progress)
        score?.transitionOut(stage, progress)
        light?.transitionOut(stage, progress)
    }

    override fun render(stage : Stage, g: Graphics) {
        background?.render(stage,  g)
        score?.render(stage, g)
        light?.render(stage, g)
    }
}