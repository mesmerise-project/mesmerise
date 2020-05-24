package cc.ekblad.fairytale
import com.beust.klaxon.Klaxon
import io.ktor.application.ApplicationCall
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondText
import java.awt.Image
import java.io.File

internal fun <T, U> T?.fmap(f : (T) -> U) : U? =
    if (this == null) null else f(this)

internal fun isImageFile(f : File) : Boolean =
    f.extension in IMAGE_FORMATS
internal val IMAGE_FORMATS =
    listOf("png", "jpg", "jpeg", "gif", "bmp")

val klaxon = Klaxon()
suspend fun ApplicationCall.respondJson(it : Any) =
    respondText(klaxon.toJsonString(it), ContentType.Application.Json)
suspend fun ApplicationCall.respondOk() =
    respondText("")
suspend fun ApplicationCall.respond404() =
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