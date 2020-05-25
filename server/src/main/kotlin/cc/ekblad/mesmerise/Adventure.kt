package cc.ekblad.mesmerise

data class Adventure(
    val name : String,
    val scenes : Map<String, Scene>
)