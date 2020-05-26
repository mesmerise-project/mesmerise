package se.mesmeri.mesmerise

import se.mesmeri.mesmerise.props.Scene

data class Adventure(
    val name : String,
    val scenes : Map<String, Scene>
)