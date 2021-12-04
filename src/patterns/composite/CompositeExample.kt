package patterns.composite

fun main() {

    val miller = Rifleman()
    val caparzo = Rifleman()
    val jackson = Sniper()

    val squad = Squad(miller, caparzo, jackson)

    println(squad.infantryUnits.size)
}

private class Squad(val infantryUnits: MutableList<InfantryUnit> = mutableListOf()) : CanCountBullets {
    constructor(vararg units: InfantryUnit) : this(mutableListOf()) {
        for (u in units) {
            this.infantryUnits.add(u)
        }
    }

    override fun bulletsLeft(): Int {
        return infantryUnits.sumOf { it.bulletsLeft() }
    }
}

private interface CanCountBullets {
    fun bulletsLeft(): Int
}

private class Bullet

private class Magazine(capacity: Int) : CanCountBullets {
    private val bullets = List(capacity) { Bullet() }

    override fun bulletsLeft(): Int = bullets.size
}

private interface InfantryUnit : CanCountBullets

private class Rifleman(initialMagazines: Int = 3) : InfantryUnit {
    private val magazines = List(initialMagazines) { Magazine(5) }
    override fun bulletsLeft(): Int {
        return magazines.sumOf { it.bulletsLeft() }
    }
}

private class Sniper(initialBullets: Int = 50) : InfantryUnit {
    private val bullets = List(initialBullets) { Bullet() }
    override fun bulletsLeft() = bullets.size
}