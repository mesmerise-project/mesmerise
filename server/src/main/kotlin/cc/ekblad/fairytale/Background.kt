package cc.ekblad.fairytale
import org.slf4j.*
import java.awt.Graphics
import java.awt.Image
import java.io.File
import javax.imageio.ImageIO

private fun loadImage(path: String): Image {
    val f = File(path)
    val c = Class.forName("cc.ekblad.fairytale.Background")
    LoggerFactory.getLogger(c).info("Loading background {}", f.absolutePath)
    return ImageIO.read(f)
}

class Background(private var originalImage : Image) : Prop {
    private val logger = LoggerFactory.getLogger(javaClass)
    private var scaledImage : Image? = null
    private val image : Image
        get() = this.scaledImage ?: this.originalImage
    constructor(path : String) : this(loadImage(path))

    override fun enterStage(screenW: Int, screenH: Int) {
        this.scaledImage = image.scaled(screenW, screenH)
    }

    override fun render(g : Graphics) {
        val x = (g.clipBounds.width - this.image.getWidth(null) as Int)/2
        val y = (g.clipBounds.height - this.image.getHeight(null) as Int)/2
        g.drawImage(this.image, x, y, null)
    }
}