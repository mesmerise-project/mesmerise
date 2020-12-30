package se.mesmeri.mesmerise
import inkapplications.shade.auth.TokenStorage
import org.ini4j.Wini
import java.net.URI
import java.nio.file.*

private const val SECTION_HUE = "PhilipsHue"
private const val SETTING_ENABLE = "enable"
private const val SETTING_URL = "url"
private const val SETTING_LIGHTS = "lights"
private const val SETTING_AUTH_TOKEN = "token"

class HueLightSet(private val ini : Wini) {
    private var lights : Set<String>
    init {
        lights = ini.get(SECTION_HUE, SETTING_LIGHTS)?.let(Utils::deserialize) ?: emptySet()
    }
    private companion object Utils {
        fun serialize(lights : Set<String>) = lights.joinToString(separator = ",")
        fun deserialize(lights : String) = lights.filter{ !it.isWhitespace() }.split(",").toSet()
    }
    fun add(id : String) {
        lights = lights + id
        ini.put(SECTION_HUE, SETTING_LIGHTS, serialize(lights))
        ini.store()
    }
    fun remove(id : String) {
        lights = lights - id
        ini.put(SECTION_HUE, SETTING_LIGHTS, serialize(lights))
        ini.store()
    }
    fun toList() : List<String> = lights.toList()
}

class Settings(backingFile : String) : TokenStorage {
    private val ini : Wini
    init {
        val path = Path.of(backingFile)
        if(!Files.exists(path)){
            Files.createFile(path)
        }
        ini = Wini(path.toFile())
    }

    private fun set(section : String, setting : String, value : Any?) {
        ini.put(section, setting, value?.toString() ?: "")
        ini.store()
    }

    private fun get(section : String, setting : String) : String? {
        val value = ini.get(section, setting)
        return if(!value.isNullOrBlank()) {
            value
        } else {
            null
        }
    }

    var enablePhilipsHue : Boolean
        get() = get(SECTION_HUE, SETTING_ENABLE).toBoolean()
        set(value) = set(SECTION_HUE, SETTING_ENABLE, value)

    var philipsHueBaseUri : URI?
        get() = get(SECTION_HUE, SETTING_URL)?.let(::URI)
        set(value) = set(SECTION_HUE, SETTING_URL, value)

    val philipsHueLights = HueLightSet(ini)

    override suspend fun getToken(): String? =
        get(SECTION_HUE, SETTING_AUTH_TOKEN)

    override suspend fun setToken(token: String?) {
        set(SECTION_HUE, SETTING_AUTH_TOKEN, token)
    }
}
