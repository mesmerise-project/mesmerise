package cc.ekblad.mesmerise

import cc.ekblad.mesmerise.props.Scene

data class Adventure(
    val name : String,
    val scenes : Map<String, Scene>
)