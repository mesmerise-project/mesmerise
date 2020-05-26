package se.mesmeri.mesmerise

data class AdventureMeta(
    val name   : String,
    val scenes : List<SceneMeta>,
    val assets : Assets
) {
    data class Assets(
        val images : List<String>,
        val music : List<String>
    )
}
