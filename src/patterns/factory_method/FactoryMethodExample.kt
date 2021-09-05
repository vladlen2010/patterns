package patterns.factory_method

import java.awt.Color
import java.awt.FlowLayout
import java.awt.Font
import javax.swing.*
import kotlin.system.exitProcess

fun main() {
    val dialog = if (System.getProperty("os.name").equals("Windows 10")) {
        WindowDialog()
    } else {
        HtmlDialog()
    }

    dialog.renderWindow()
}

//region buttons

interface Button {
    fun render()
    fun onClick()
}

private class HtmlButton() : Button {
    override fun render() {
        println("<button>Test Button</button>")
        onClick()
    }

    override fun onClick() = println("Click! Button says - 'Hello World!'")
}

private class WindowsButton() : Button {
    val panel = JPanel()
    val frame = JFrame()
    private lateinit var button: JButton

    override fun render() {
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        val label = JLabel("Hello world!")
        with(label) {
            isOpaque = true
            background = Color(235, 233, 126)
            font = Font("Dialog", Font.BOLD, 44)
            horizontalAlignment = SwingConstants.CENTER
        }
        panel.layout = FlowLayout(FlowLayout.CENTER)
        frame.contentPane.add(panel)
        panel.add(label)
        onClick()
        panel.add(button)

        frame.setSize(320, 200)
        frame.isVisible = true
        onClick()
    }

    override fun onClick() {
        JButton().addActionListener {
            frame.isVisible = true
            exitProcess(0)
        }
    }
}

//endregion

//region factory

abstract class Dialog() {

    fun renderWindow() {
        /*
        ...
         */

        val okButton = createButton()
        okButton.render()
    }

    abstract fun createButton(): Button
}

private class HtmlDialog : Dialog() {
    override fun createButton() = HtmlButton()
}

private class WindowDialog : Dialog() {
    override fun createButton() = WindowsButton()
}

//endregion