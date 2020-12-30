package se.mesmeri.mesmerise.props
import com.github.ajalt.colormath.*
import inkapplications.shade.constructs.*
import kotlin.math.*

// TODO: use an interface for light
class Light private constructor(
    val color: Color? = null,
    val colorTemp: ColorTemperature? = null,
    val brightness: Percentage? = null,
    val on: Boolean = true
) : Prop {
    override fun enterStage(stage: Stage) =
        stage.setLight(this)

    companion object {
        private val defaultBrightness = 80.percent
        fun of(light: String): Light? {
            val l = light.toLowerCase().filter { !it.isWhitespace() }
            return when(l) {
                "campfire"   -> campfire
                "office"     -> office
                "candle"     -> candle
                "forest"     -> forest
                "darkforest" -> darkForest
                "sunny"      -> sunny
                "overcast"   -> overcast
                "sunset"     -> sunset
                "snow"       -> snow
                "night"      -> night
                "red"        -> red
                "green"      -> green
                "blue"       -> blue
                "pink"       -> pink
                "off"        -> off
                else         -> tryParseLight(l)
            }
        }

        /**
         * Parse a light of the format color:brightness, where
         * color is a hexadecimal color code and brightness is a floating
         * point number clamped to [0, 1].
         * Example: ff0000:1.0
         */
        private fun tryParseLight(l: String): Light? {
            val parts = l.split(":")
            if(parts.size > 2 || parts[0].length != 6) {
                return null
            }
            val colorVal = parts[0].toInt(radix = 16)
            val blue = colorVal and 0xff
            val green = (colorVal shr 8) and 0xff
            val red = colorVal shr 16
            val brightness = if(parts.size == 2) {
                parts[1].toFloatOrNull()?.let {
                    min(100.0, max(0.0, it*100.0)).percent
                }
            } else null
            return Light(
                color = RGB(red, green, blue),
                brightness = brightness
            )
        }

        val off = Light(on = false)
        val red = Light(
            color = RGB(255, 0, 0),
            brightness = defaultBrightness
        )
        val green = Light(
                color = RGB(0, 255, 0),
                brightness = defaultBrightness
        )
        val blue = Light(
            color = RGB(0, 0, 255),
            brightness = defaultBrightness
        )
        val pink = Light(
            color = RGB(255, 105, 180),
            brightness = defaultBrightness
        )
        val campfire = Light(
            brightness = 75.percent,
            colorTemp = 2500.kelvin
        )
        val office = Light(
            brightness = 85.percent,
            colorTemp = 3500.kelvin
        )
        val candle = Light(
            brightness = 75.percent,
            colorTemp = 2100.kelvin
        )
        val sunny = Light(
            brightness = 100.percent,
            colorTemp = 5500.kelvin
        )
        val snow = Light(
            brightness = 100.percent,
            colorTemp = 8000.kelvin
        )
        val overcast = Light(
            brightness = 90.percent,
            colorTemp = 7000.kelvin
        )
        val sunset = Light(
            colorTemp = 1850.kelvin,
            brightness = 100.percent
        )
        val night = Light(
            color = RGB(230, 230, 255),
            brightness = 25.percent
        )
        val forest = Light(
            color = RGB(192, 255, 200),
            brightness = 80.percent
        )
        val darkForest = Light(
                color = RGB(192, 255, 220),
                brightness = 60.percent
        )
    }
}