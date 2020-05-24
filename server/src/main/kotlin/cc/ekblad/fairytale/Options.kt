package cc.ekblad.fairytale
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default

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
        ).default(".")
    val loglevel by parser
        .storing(
            "-L", "--loglevel",
            argName="LEVEL",
            help="Minimum log level to print (DEBUG/INFO/WARN/ERROR)"
        ).default("WARN")
}