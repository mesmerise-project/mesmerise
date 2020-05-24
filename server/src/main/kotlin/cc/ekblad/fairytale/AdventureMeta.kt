package cc.ekblad.fairytale

data class AdventureMeta(
    val name   : String,
    val scenes : List<SceneMeta>,
    val assets : AdventureMeta.Assets
) {
    data class Assets(
        val images : List<String>,
        val music : List<String>
    )
}
