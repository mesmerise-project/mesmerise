package se.mesmeri.mesmerise.props
import java.awt.Graphics

interface Stage {
    val screenW : Int
    val screenH : Int
    fun setLight(light : Light)
}

/**
 * A prop is something used in a scene: an image, background music, etc.
 */
interface Prop {
    /**
     * Called when the prop is told to enters the stage.
     */
    fun enterStage(stage : Stage) {}

    /**
     * Called periodically while the prop is entering the stage.
     * @param progress Progress towards completing the transition, ranging
     *                 from 0.0 to 1.0.
     */
    fun transitionIn(stage : Stage, progress : Double) {}

    /**
     * Called periodically while the prop is leaving the stage.
     * @param progress Progress towards completing the transition, ranging
     *                 from 0.0 to 1.0.
     */
    fun transitionOut(stage : Stage, progress : Double) {}

    /**
     * Called when the prop is told to leave the stage.
     */
    fun exitStage(stage : Stage) {}

    /**
     * Called whenever the prop needs to be redrawn.
     */
    fun render(stage : Stage, g : Graphics) {}
}