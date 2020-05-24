package cc.ekblad.fairytale
import ch.qos.logback.classic.*
import com.xenomachina.argparser.*
import io.ktor.application.call
import io.ktor.http.content.*
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.slf4j.LoggerFactory

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

fun main(args : Array<String>) {
    mainBody {
        val logger = LoggerFactory.getLogger("cc.ekblad.fairytale")
        val opts = Options(ArgParser(args))
        setLogLevel(opts.loglevel)
        logger.info("***Starting Fairytale***")
        logger.info("Library path: {}", opts.library)
        logger.info("HTTP port: {}", opts.port)
        logger.info("Log level: {}", opts.loglevel)

        val viewer = Viewer()
        val library = Library(opts.library)
        viewer.start()
        val service = AdventureService(viewer, library)

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