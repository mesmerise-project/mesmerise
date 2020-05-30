package se.mesmeri.mesmerise
import ch.qos.logback.classic.*
import com.xenomachina.argparser.*
import inkapplications.shade.Shade
import io.ktor.application.call
import io.ktor.http.content.*
import io.ktor.request.receiveText
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.runBlocking
import org.ini4j.Wini
import org.slf4j.LoggerFactory
import se.mesmeri.mesmerise.props.Light
import java.io.File
import kotlin.reflect.KClass
import kotlin.system.exitProcess

fun setLogLevel(level : String) {
    val l = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
    l.level = when(level.toUpperCase()) {
        "DEBUG" -> Level.DEBUG
        "INFO"  -> Level.INFO
        "WARN"  -> Level.WARN
        "ERROR" -> Level.ERROR
        else    -> Level.WARN
    }
}

fun main(args : Array<String>): Unit = mainBody {
    runBlocking {
        val opts = Options(ArgParser(args))
        setLogLevel(opts.loglevel)

        val logger = LoggerFactory.getLogger("se.mesmeri.mesmerise")
        logger.info("***Starting Mesmerise***")
        logger.info("Library path: {}", opts.library)
        logger.info("HTTP port: {}", opts.port)
        logger.info("Log level: {}", opts.loglevel)
        logger.info("Configuration: {}", opts.config)

        val settings = Settings(opts.config)
        val viewer = Viewer { w, h -> LitStage(settings, w, h) }
        val library = Library(opts.library)
        val service = AdventureService(viewer, library, settings)

        viewer.start()
        // TODO: refactor; put actions between routes and services
        val server = embeddedServer(Netty, port = opts.port) {
            routing {
                route("/api") {
                    get("/adventures") {
                        service.listAdventures(call)
                    }
                    get("/adventures/{name}") {
                        service.loadAdventure(call, call.parameters["name"]!!)
                    }
                    get("/adventures/{adv}/scenes/{scene}") {
                        service.setScene(
                            call,
                            call.parameters["adv"]!!,
                            call.parameters["scene"]!!
                        )
                    }
                    get("/adventures/{adv}/scenes/{scene}/thumbnail") {
                        service.getSceneThumbnail(
                            call,
                            call.parameters["adv"]!!,
                            call.parameters["scene"]!!
                        )
                    }
                    get("/silence") {
                        service.killMusic(call)
                    }
                    get("/unsilence") {
                        service.restartMusic(call)
                    }
                    route("/lights") {
                        get("/authenticate") { service.authLights(call) }
                        get { service.getLights(call) }
                        put("/enabled") {
                            val enable = call.receiveText() == "true"
                            service.enablePhilipsHue(call, enable)
                        }
                        put("/url") {
                            val url = call.receiveText()
                            service.setHueBaseUrl(call, url)
                        }
                        put("/{id}/enabled") {
                            val id = call.parameters["id"]!!
                            val enable = call.receiveText() == "true"
                            service.enableLight(call, id, enable)
                        }
                    }
                }
                static("/") {
                    resource("/", "client/index.html")
                    resources("client")
                }
            }
        }
        server.start(wait = true)
    }
}