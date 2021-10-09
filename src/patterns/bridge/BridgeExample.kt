package patterns.bridge

fun main() {

    val rifleman = Soldier(Rifle(), RegularLegs())
    val grenadier = Soldier(Grenade(), RegularLegs())
    val upgradedGrenadier = Soldier(GrenadePack(), RegularLegs())
    val upgradedRifleman = Soldier(MachineGun(), RegularLegs())
    val lightRifleman = Soldier(Rifle(), AthleticLegs())
    val lightGrenadier = Soldier(Grenade(), AthleticLegs())
}

private interface Infantry {
    fun move(x: Long, y: Long)

    fun attack(x: Long, y: Long)
}

private typealias PointsOfDamage = Long
private typealias Meters = Int

interface Weapon {
    fun causeDamage(): PointsOfDamage
}

interface Legs {
    fun move(): Meters
}

private const val GRENADE_DAMAGE: PointsOfDamage = 5L
private const val RIFLE_DAMAGE = 3L
private const val REGULAR_SPEED: Meters = 1

private class Grenade : Weapon {
    override fun causeDamage() = GRENADE_DAMAGE
}

private class GrenadePack : Weapon {
    override fun causeDamage() = GRENADE_DAMAGE * 3
}

private class Rifle : Weapon {
    override fun causeDamage() = RIFLE_DAMAGE
}

private class MachineGun : Weapon {
    override fun causeDamage() = RIFLE_DAMAGE * 2
}

private class RegularLegs : Legs {
    override fun move() = REGULAR_SPEED
}

private class AthleticLegs : Legs {
    override fun move() = REGULAR_SPEED * 2
}

private class Soldier(
    private val weapon: Weapon,
    private val legs: Legs
) : Infantry {
    override fun attack(x: Long, y: Long) {
        // Find target
        // Shoot
        weapon.causeDamage()
    }

    override fun move(x: Long, y: Long) {
        // Compute direction
        // Move at its own pace
        legs.move()
    }
}