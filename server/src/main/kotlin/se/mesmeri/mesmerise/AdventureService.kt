package se.mesmeri.mesmerise
import io.ktor.application.*
import io.ktor.http.HttpStatusCode
import io.ktor.response.*
import java.net.URI

class AdventureService(
    private val viewer : Viewer,
    private val library : Library,
    private val settings : Settings
) {
    private var adventure = null as Adventure?

    private fun ensureAdventureLoaded(name : String, reload : Boolean = false) : Boolean {
        if(reload || adventure?.name != name) {
            adventure = library.loadAdventure(name)
        }
        return adventure?.name == name
    }

    suspend fun loadAdventure(
        call : ApplicationCall,
        adventureName : String,
        reload : Boolean = false
    ) {
        if (ensureAdventureLoaded(adventureName, reload)) {
            call.respondJson(library.getAdventureMeta(adventureName)!!)
        } else {
            call.respond404()
        }
    }

    suspend fun listAdventures(call : ApplicationCall) {
        call.respondJson(library.adventures)
    }

    suspend fun killMusic(call : ApplicationCall) {
        viewer.silence()
        call.respondOk()
    }

    suspend fun restartMusic(call : ApplicationCall) {
        viewer.unsilence()
        call.respondOk()
    }

    suspend fun setScene(
        call: ApplicationCall,
        adventureName: String,
        scene: String
    ) {
        if(ensureAdventureLoaded(adventureName)) {
            val success = adventure?.scenes?.get(scene)?.let {
                viewer.setScene(it) ; true
            } ?: false
            if(success) {
                call.respondOk()
            } else {
                call.respond404()
            }
        } else {
            call.respond404()
        }
    }

    suspend fun getSceneThumbnail(
        call : ApplicationCall,
        adventure : String,
        scene : String
    ) {
        val file = library.getSceneThumbnail(adventure, scene)
        if(file != null) {
            call.respondFile(file)
        } else {
            call.respond404()
        }
    }

    suspend fun getLights(call : ApplicationCall) {
        val shade = settings.createShade(true)
        if(shade != null) {
            val lights = shade.lights.getLights()
            val lightInfos = lights.map {
                LightInfo(it.key, it.value.name)
            }
            call.respondJson(lightInfos)
        } else {
            call.respond(
                HttpStatusCode.ExpectationFailed,
                "No Philips Hue configuration"
            )
        }
    }

    suspend fun enableLight(
        call : ApplicationCall,
        id: String,
        enable : Boolean
    ) {
        val light = settings.createShade(true)?.lights?.getLight(id)
        if(light != null) {
            if(enable) {
                settings.philipsHueLights.add(id)
            } else {
                settings.philipsHueLights.remove(id)
            }
            call.respondOk()
        } else {
            call.respond404()
        }
    }

    suspend fun setHueBaseUrl(call : ApplicationCall, url : String) {
        settings.philipsHueBaseUri = URI(url)
        call.respondOk()
    }

    suspend fun enablePhilipsHue(call : ApplicationCall, enable : Boolean) {
        settings.enablePhilipsHue = enable
        call.respondOk()
    }

    suspend fun authLights(call : ApplicationCall) {
        val shade = settings.createShade(true)!!
        shade.auth.awaitToken()
        try {
            shade.lights.getLights()
            call.respondOk()
        } catch(e : Exception) {
            call.respond(HttpStatusCode.Unauthorized)
        }
    }
}