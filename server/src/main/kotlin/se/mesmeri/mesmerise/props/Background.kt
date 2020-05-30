package se.mesmeri.mesmerise.props
import se.mesmeri.mesmerise.scaled
import org.slf4j.*
import java.awt.Graphics
import java.awt.Image
import java.io.File
import javax.imageio.ImageIO

private fun loadImage(path: String): Image {
    val f = File(path)
    val c = Background::class.java
    LoggerFactory.getLogger(c).info("Loading background {}", f.absolutePath)
    return ImageIO.read(f)
}

class Background(private var originalImage : Image) : Prop {
    private val logger = LoggerFactory.getLogger(javaClass)
    private var scaledImage : Image? = null
    private val image : Image
        get() = this.scaledImage ?: this.originalImage
    constructor(path : String) : this(loadImage(path))

    override fun enterStage(stage : Stage) {
        this.scaledImage = image.scaled(stage.screenW, stage.screenH)
    }

    override fun render(stage : Stage, g : Graphics) {
        val x = (g.clipBounds.width - this.image.getWidth(null) as Int)/2
        val y = (g.clipBounds.height - this.image.getHeight(null) as Int)/2
        g.drawImage(this.image, x, y, null)
    }
}