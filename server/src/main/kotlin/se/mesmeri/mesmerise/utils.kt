package se.mesmeri.mesmerise
import com.beust.klaxon.Klaxon
import inkapplications.shade.Shade
import io.ktor.application.ApplicationCall
import io.ktor.http.*
import io.ktor.response.respondText
import org.slf4j.Logger
import java.awt.Image
import java.io.File
import kotlin.system.exitProcess

internal fun isImageFile(f : File) : Boolean =
    f.extension in IMAGE_FORMATS
internal val IMAGE_FORMATS =
    listOf("png", "jpg", "jpeg", "gif", "bmp")

internal val klaxon = Klaxon()
internal suspend fun ApplicationCall.respondJson(it : Any) =
    respondText(klaxon.toJsonString(it), ContentType.Application.Json)
internal suspend fun ApplicationCall.respondOk() =
    respondText("")
internal suspend fun ApplicationCall.respond404() =
    respondText("not found", status = HttpStatusCode.NotFound)

internal fun Image.scaled(width : Int, height : Int) : Image {
    var w = this.getWidth(null)
    var h = this.getHeight(null)
    val screenRatio = width/height
    val imageRatio = w/h
    if(screenRatio < imageRatio) {
        h = ((width / w.toDouble()) * h).toInt()
        w = width
    } else {
        w = ((height / h.toDouble()) * w).toInt()
        h = height
    }
    return this.getScaledInstance(w, h, Image.SCALE_SMOOTH)
}

internal fun Logger.die(msg : String) : Any {
    this.error(msg)
    exitProcess(-1)
}

internal fun Settings.createShade(force : Boolean = false) : Shade? {
    if(!enablePhilipsHue && !force) {
        return null
    }
    return philipsHueBaseUri?.let {
        Shade(
            initBaseUrl = it.toString(),
            storage = this
        )
    }
}