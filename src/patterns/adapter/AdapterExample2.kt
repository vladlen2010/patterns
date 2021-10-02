package patterns.adapter

import kotlin.math.pow
import kotlin.math.sqrt

fun main() {

    val hole = RoundHole(5.0)
    val rpeg = RoundPeg(5.0)

    if (hole.fits(rpeg)) {
        println("Round peg r5 fits round hole r5.")
    }

    val smallSqPeg = SquarePeg(2.0)
    val largeSqPeg = SquarePeg(20.0)

    val smallAdapter = SquarePegAdapter(smallSqPeg)
    val largeAdapter = SquarePegAdapter(largeSqPeg)

    if (hole.fits(smallAdapter)) {
        println("Square peg w2 fits round hole r5.")
    }
    if (!hole.fits(largeAdapter)) {
        println("Square peg w20 does not fit into round hole r5.")
    }
}

//region round

private class RoundHole(
    private val radius: Double
) {
    fun fits(peg: IRoundPeg): Boolean {
        return radius >= peg.getRadius()
    }
}

interface IRoundPeg {
    fun getRadius(): Double
}

class RoundPeg(
    private val radius: Double
) : IRoundPeg {
    override fun getRadius() = radius
}

//endregion

//region square

class SquarePeg(
    private val width: Double
) {

    fun getWidth() = width

    fun getSquare(): Double = width.pow(2)
}

//endregion

//region adapters

class SquarePegAdapter(
    private val peg: SquarePeg
) : IRoundPeg {
    override fun getRadius() = sqrt((peg.getWidth() / 2).pow(2) * 2)
}

//endregion
