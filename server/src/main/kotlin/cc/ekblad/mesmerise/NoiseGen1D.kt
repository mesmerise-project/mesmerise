package cc.ekblad.mesmerise

interface NoiseGen1D {
    fun noise(x : Double) : Double
    fun saltedNoise(x : Double, salt : Int) : Double
}