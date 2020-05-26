package cc.ekblad.mesmerise
import java.awt.*
import kotlin.math.*

// Bresenham's line drawing algorithm, augmented with noise and broken lines.
fun Graphics2D.noisyLine(
    x0 : Int, y0 : Int, x1 : Int, y1 : Int,
    noiseGen : NoiseGen1D,
    seedScaleX : Double = 1.0,
    seedScaleY : Double= 1.0,
    noiseScaleX : Double = 1.0,
    noiseScaleY : Double= 1.0,
    segmentLength : Int = 1,
    segmentSpacing : Int = 0
) {
    var x = x0
    var y = y0
    val dx =  abs(x1-x0)
    val sx = if(x0<x1) 1 else -1
    val dy = -abs(y1-y0)
    val sy = if(y0<y1) 1 else -1
    var err = dx+dy
    var draw = segmentLength
    var skip = 0
    while (true) {
        val offsetX = noiseGen.saltedNoise(y*seedScaleY, 2)*noiseScaleX
        val offsetY = noiseGen.saltedNoise(x*seedScaleX, 3)*noiseScaleY
        if(skip <= 0) {
            this.drawLine(
                (x + offsetX).toInt(),
                (y + offsetY).toInt(),
                (x + offsetX).toInt(),
                (y + offsetY).toInt()
            )
            draw = draw-1
            if(draw <= 0 ) {
                skip = segmentSpacing
            }
        } else {
            skip = skip-1
            if(skip <= 0) {
                draw = segmentLength
            }
        }
        if (x == x1 && y == y1) {
            break
        }
        val e2 = 2 * err;
        if (e2 >= dy) {
            err += dy
            x += sx
        }
        if (e2 <= dx) {
            err += dx
            y += sy
        }
    }
}

class TravelMap(
    private val background : Prop,
    private val noiseGen : NoiseGen1D = PerlinNoiseGen(3, 0.25),
    private val smoothness : Double = 0.5,
    private val detours : Double = 0.25
) : Prop {
    override fun enterStage(screenW: Int, screenH: Int) =
        background.enterStage(screenW, screenH)
    override fun exitStage() =
        background.exitStage()
    override fun transitionIn(progress: Double) =
        background.transitionIn(progress)
    override fun transitionOut(progress: Double) =
            background.transitionOut(progress)

    override fun render(g: Graphics) {
        TODO("How should map lines be defined and drawn?")
        val g2d = g as Graphics2D
        background.render(g2d)
        g2d.color = Color.red
        g2d.stroke = BasicStroke(3F)
        val seedScalingFactor = (1-max(0.0001, min(1.0, smoothness)))/100
        val scalingFactor = max(0.0, min(1.0, detours))*255
        g2d.noisyLine(
        123, 1091, 514, 39,
            noiseGen = noiseGen,
            seedScaleX = seedScalingFactor,
            seedScaleY = seedScalingFactor,
            noiseScaleX = scalingFactor,
            noiseScaleY = scalingFactor,
            segmentSpacing = 6,
            segmentLength = 12
        )
    }
}