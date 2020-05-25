package cc.ekblad.mesmerise
import org.ini4j.Wini
import org.slf4j.LoggerFactory
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class Library(private val path : String) {
    private val MUSIC_DIR = "music"
    private val IMAGE_DIR = "images"
    private val SCENE_FILE = "scenes.ini"
    private val THUMBNAIL_DIR = "thumbnails"
    private val THUMBNAIL_SIZE = 200

    private val logger = LoggerFactory.getLogger(javaClass)
    val adventures : List<String>
        get() {
            val dir = File(this.path)
            return if(!dir.isDirectory) {
                logger.warn("Library path is not a directory: {}", dir.absolutePath)
                listOf()
            } else {
                dir.list().filter {
                    File("${dir.absolutePath}/$it").isDirectory
                }
            }
        }

    fun getAdventureMeta(adventureName : String) : AdventureMeta? {
        val dir = File("${this.path}/$adventureName")
        logger.info("Loading adventure {}", dir.absolutePath)
        return if (dir.isDirectory) {
            AdventureMeta(
                name = adventureName,
                scenes = loadScenes("$dir"),
                assets = AdventureMeta.Assets(
                    images = listFiles(dir, IMAGE_DIR, ::isImageFile),
                    music = listFiles(dir, MUSIC_DIR) { it.extension == "mp3" }
                )
            )
        } else {
            logger.error("Tried to load adventure from non-directory: {}", dir.absolutePath)
            null
        }
    }

    fun loadAdventure(adventureName : String) : Adventure? {
        return getAdventureMeta(adventureName).fmap(::loadAdventure)
    }

    fun loadAdventure(adventure : AdventureMeta) : Adventure {
        logger.debug("Loading adventure {}", adventure.name)
        val scenes = adventure.scenes.map {
            mkScene(adventure.name, it)
        }
        val autoThumbFile = thumbdirAutogenFile(adventure.name)
        if(autoThumbFile.isFile) {
            logger.debug("Thumbnail directory for {} was created automatically; deleting...", adventure.name)
            File(autoThumbFile.parent).deleteRecursively()
        }
        return Adventure(adventure.name, scenes.toMap())
    }

    private fun ensureThumbDirExists(adventure : String) : File {
        val dir = File("$path/$adventure/$THUMBNAIL_DIR")
        if(!dir.isDirectory) {
            logger.info("Thumbnail dir for {} does not exist; creating...", adventure)
            dir.mkdir()
            thumbdirAutogenFile(adventure).createNewFile()
        }
        return dir
    }

    private fun thumbdirAutogenFile(adventure: String) : File {
        return File("$path/$adventure/$THUMBNAIL_DIR/.auto_generated")
    }

    private fun createThumbnail(
        adventure : String,
        scene : String,
        image : File
    ) : File {
        val dir = ensureThumbDirExists(adventure)
        val tnfile = File("${dir.absolutePath}/$scene.${image.extension}")
        if(!tnfile.isFile) {
            logger.info("Thumbnail for {} does not exist; creating...", scene)
            val tn = ImageIO.read(image).scaled(THUMBNAIL_SIZE, THUMBNAIL_SIZE)
            writeImage(tn, tnfile)
        }
        return tnfile
    }

    private fun writeImage(image : Image, file : File) {
        val outputImage = BufferedImage(
            image.getWidth(null),
            image.getHeight(null),
            BufferedImage.TYPE_INT_RGB
        )
        outputImage.createGraphics().drawImage(image, null, null)
        ImageIO.write(outputImage, file.extension, file)
    }

    fun getSceneThumbnail(
        adventure : String,
        scene : String
    ) : File? {
        return getAdventureMeta(adventure).fmap {
            getSceneThumbnail(it, scene)
        }
    }

    fun getSceneThumbnail(
            adventure : AdventureMeta,
            scene : String
    ) : File? {
        return adventure.scenes
            .firstOrNull { it.name == scene }
            ?.background
            .fmap {
                createThumbnail(
                    adventure.name,
                    scene,
                    File("$path/${adventure.name}/$IMAGE_DIR/$it")
                )
            }
    }

    private fun loadScenes(path : String) : List<SceneMeta> {
        val sceneFile = File("$path/$SCENE_FILE")
        if(!sceneFile.exists()) {
            logger.warn("Adventure {} contains no scene file", File(path).absolutePath)
            return listOf()
        }
        return Wini(sceneFile).map {
            logger.debug("Reading scene {}", it.key)
            SceneMeta(
                name = it.key,
                background = it.value["background"],
                music = it.value["music"]
            )
        }.sortedBy { it.name }
    }

    private fun listFiles(adventureDir: File, subdir : String, p : (File) -> Boolean): List<String> {
        val dir = File("${adventureDir.absolutePath}/$subdir")
        if(!dir.isDirectory) {
            logger.warn("Adventure contains no '$subdir' directory: {}", adventureDir.absolutePath)
            return listOf()
        }
        return dir.list().filter { val f = File(it); f.isFile && p(f)}
    }

    private fun mkScene(adventure : String, s: SceneMeta) : Pair<String, Scene> {
        val bg = s.background.fmap {
            Background("$path/$adventure/$IMAGE_DIR/$it")
        }
        val score = s.music.fmap {
            Song("$path/$adventure/$MUSIC_DIR/$it")
        }
        return Pair(s.name, Scene(bg, score))
    }
}