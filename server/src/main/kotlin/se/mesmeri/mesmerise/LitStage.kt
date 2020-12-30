package se.mesmeri.mesmerise
import inkapplications.shade.constructs.Coordinates
import inkapplications.shade.lights.LightStateModification
import kotlinx.coroutines.runBlocking
import se.mesmeri.mesmerise.props.Light
import se.mesmeri.mesmerise.props.Stage

class LitStage(
        private var settings : Settings,
        override var screenW: Int,
        override var screenH: Int
) : Stage {
    // TODO: move to coroutines
    override fun setLight(light: Light) = runBlocking {
        val shade = settings.createShade() ?: return@runBlocking
        val lights = settings.philipsHueLights.toList()
        lights.forEach {
            try {
                shade.lights.setState(
                    it,
                    LightStateModification(
                        cieColorCoordinates = light.color?.let(::Coordinates),
                        colorTemperature = light.colorTemp,
                        brightness = light.brightness,
                        on = light.on
                    )
                )
            } catch(e : Exception) {
                // Just eat Hue errors for now
            }
        }
    }
}