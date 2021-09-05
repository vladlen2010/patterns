package patterns.abstract_factory

import java.util.*

fun main() {
    Application.configureApplication().paint()
}

//region buttons

private interface Button {
    fun paint()
}

private class MacOSButton : Button {
    override fun paint() = println("You have created MacOSButton.")
}

private class WindowsButton : Button {
    override fun paint() = println("You have created WindowsButton.")
}

//endregion

//region checkboxes

private interface Checkbox {
    fun paint()
}

private class MacOSCheckbox : Checkbox {
    override fun paint() = println("You have created MacOSCheckbox.")
}

private class WindowsCheckbox : Checkbox {
    override fun paint() = println("You have created WindowsCheckbox.")
}

//endregion

//region factories

/**
 * This is abstract factory
 */
private interface GUIFactory {
    fun createButton(): Button
    fun createCheckBox(): Checkbox
}

private class MacOSFactory : GUIFactory {
    override fun createButton() = MacOSButton()
    override fun createCheckBox() = MacOSCheckbox()
}

private class WindowsFactory() : GUIFactory {
    override fun createButton() = WindowsButton()
    override fun createCheckBox() = WindowsCheckbox()
}

//endregion

private class Application(factory: GUIFactory) {

    companion object {
        fun configureApplication(): Application {
            val osName = System.getProperty("os.name").lowercase(Locale.getDefault())
            return if (osName.contains("mac")) {
                Application(MacOSFactory())
            } else {
                Application(WindowsFactory())
            }
        }
    }

    private var button: Button
    private var checkbox: Checkbox

    init {
        button = factory.createButton()
        checkbox = factory.createCheckBox()
    }

    fun paint() {
        button.paint()
        checkbox.paint()
    }
}