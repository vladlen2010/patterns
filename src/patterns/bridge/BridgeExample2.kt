package patterns.bridge

fun main() {
    testDevice(Tv())
    testDevice(Radio())
}

private fun testDevice(device: Device) {
    println("Tests with basic remote.")
    val basicRemote = BasicRemote(device)
    basicRemote.power()
    device.printStatus()

    println("Tests with advanced remote.")
    val advancedRemote = AdvancedRemote(device)
    advancedRemote.power()
    advancedRemote.mute()
    device.printStatus()
}

//region devices

interface Device {
    fun isEnabled(): Boolean
    fun enable()
    fun disable()
    fun getVolume(): Int
    fun setVolume(percent: Int)
    fun getChannel(): Int
    fun setChannel(channel: Int)
    fun printStatus()
}

private abstract class BaseDevice : Device {
    private var on = false
    private var volume = 30
    private var channel = 1

    override fun isEnabled() = on

    override fun enable() {
        on = true
    }

    override fun disable() {
        on = false
    }

    override fun getVolume() = volume

    override fun setVolume(percent: Int) {
        volume = when {
            percent > 100 -> 100
            percent < 0 -> 0
            else -> percent
        }
    }

    override fun getChannel() = channel

    override fun setChannel(channel: Int) {
        this.channel = channel
    }

    override fun printStatus() {
        println("------------------------------------")
        println("| I'm ${javaClass.simpleName}.")
        println("| I'm " + if (on) "enabled" else "disabled")
        println("| Current volume is $volume%")
        println("| Current channel is $channel")
        println("------------------------------------\n")
    }
}

private class Radio : BaseDevice()

private class Tv : BaseDevice()

//endregion

//region remotes

interface Remote {
    fun power()
    fun volumeDown()
    fun volumeUp()
    fun channelDown()
    fun channelUp()
}

private open class BasicRemote(
    protected val device: Device
) : Remote {

    override fun power() {
        println("Remote: power toggle")
        if (device.isEnabled()) {
            device.disable()
        } else {
            device.enable()
        }
    }

    override fun volumeDown() {
        println("Remote: volume down")
        device.setVolume(device.getVolume() - 10)
    }

    override fun volumeUp() {
        println("Remote: volume up")
        device.setVolume(device.getVolume() + 10)
    }

    override fun channelDown() {
        println("Remote: channel down")
        device.setChannel(device.getChannel() - 1)
    }

    override fun channelUp() {
        println("Remote: channel up")
        device.setChannel(device.getChannel() + 1)

    }
}

private class AdvancedRemote(device: Device) : BasicRemote(device) {

    fun mute() {
        println("Remote: mute")
        device.setVolume(0)
    }
}

//endregion