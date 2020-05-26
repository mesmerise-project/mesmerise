package se.mesmeri.mesmerise.props
import java.awt.Graphics

/**
 * A prop is something used in a scene: an image, background music, etc.
 */
interface Prop {
    /**
     * Called when the prop is told to enters the stage.
     */
    fun enterStage(screenW : Int, screenH : Int) {}

    /**
     * Called periodically while the prop is entering the stage.
     * @param progress Progress towards completing the transition, ranging
     *                 from 0.0 to 1.0.
     */
    fun transitionIn(progress : Double) {}

    /**
     * Called periodically while the prop is leaving the stage.
     * @param progress Progress towards completing the transition, ranging
     *                 from 0.0 to 1.0.
     */
    fun transitionOut(progress : Double) {}

    /**
     * Called when the prop is told to leave the stage.
     */
    fun exitStage() {}

    /**
     * Called whenever the prop needs to be redrawn.
     */
    fun render(g : Graphics) {}
}