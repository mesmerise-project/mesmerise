package cc.ekblad.mesmerise
import io.ktor.application.*
import io.ktor.response.respondFile

class AdventureService(
    private val viewer : Viewer,
    private val library : Library
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
            val success = adventure?.scenes?.get(scene).fmap {
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
}