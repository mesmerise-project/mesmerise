package se.mesmeri.mesmerise
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import java.io.File
import java.net.URI
import java.nio.file.Paths

val JAR_URI = URI(
    Options::class.java
    .getResource("").path
    .split("!")
    .first()
)
val JAR_DIR = if(JAR_URI.isAbsolute) {
    File(JAR_URI).parent
} else {
    "."
}

class Options(parser : ArgParser) {
    val port by parser
        .storing(
            "-p", "--port",
            argName="PORT",
            help="HTTP port for user interface"
        ) { toInt() }
        .default(8080)
    val library by parser
        .storing("-l", "--library",
            argName="PATH",
            help="Path to adventure library"
        ).default(Paths.get(JAR_DIR, "adventures").toString())
    val loglevel by parser
        .storing(
            "-L", "--loglevel",
            argName="LEVEL",
            help="Minimum log level to print (DEBUG/INFO/WARN/ERROR)"
        ).default("WARN")
}