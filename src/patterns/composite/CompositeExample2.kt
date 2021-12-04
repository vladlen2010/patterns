package patterns.composite

import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.BorderFactory
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants

fun main() {

    ImageEditor().loadShapes(
        Circle(10, 10, 10, Color.BLUE),

        CompoundShape(
            Circle(110, 110, 50, Color.RED),
            Dot(160, 160, Color.RED)
        ),

        CompoundShape(
            Rectangle(250, 250, 100, 100, Color.GREEN),
            Dot(240, 240, Color.GREEN),
            Dot(240, 360, Color.GREEN),
            Dot(360, 360, Color.GREEN),
            Dot(360, 240, Color.GREEN)
        )
    )

}

//region shapes

private interface Shape {
    fun getX(): Int
    fun getY(): Int
    fun getWidth(): Int
    fun getHeight(): Int
    fun move(x: Int, y: Int)
    fun isInsideBounds(x: Int, y: Int): Boolean
    fun select()
    fun unselect()
    fun isSelected(): Boolean
    fun paint(graphics: Graphics)
}

private abstract class BaseShape(
    private var _x: Int,
    private var _y: Int,
    private val color: Color,
) : Shape {

    private var selected: Boolean = false

    override fun getX(): Int = _x
    override fun getY(): Int = _y

    override fun getWidth(): Int = 0
    override fun getHeight(): Int = 0

    override fun move(x: Int, y: Int) {
        this._x += x
        this._y += x
    }

    override fun isInsideBounds(x: Int, y: Int): Boolean {
        return x > getX() && x < (getX() + getWidth()) && y > getY() && y < (getY() + getHeight())
    }

    override fun select() {
        selected = true
    }

    override fun unselect() {
        selected = false
    }

    override fun isSelected(): Boolean = selected

    override fun paint(graphics: Graphics) {
        if (isSelected()) {
            enableSelectionStyle(graphics)
        } else {
            disableSelectionStyle(graphics)
        }
    }

    fun enableSelectionStyle(graphics: Graphics) {
        graphics.color = Color.LIGHT_GRAY
        val g2: Graphics2D = graphics as Graphics2D
        val dash1 = FloatArray(1) { 2.0f }
        g2.stroke = BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, dash1, 0.0f)
    }

    fun disableSelectionStyle(graphics: Graphics) {
        graphics.color = color
        val g2: Graphics2D = graphics as Graphics2D
        g2.stroke = BasicStroke()
    }
}

private class Dot(val _x: Int, val _y: Int, color: Color) : BaseShape(_x, _y, color) {
    companion object {
        private const val DOT_SIZE = 3
    }

    override fun getWidth(): Int = DOT_SIZE
    override fun getHeight(): Int = DOT_SIZE

    override fun paint(graphics: Graphics) {
        super.paint(graphics)
        graphics.fillRect(_x - 1, _y - 1, getWidth(), getHeight())
    }
}

private class Circle(
    private val radius: Int,
    val _x: Int,
    val _y: Int,
    color: Color
) : BaseShape(_x, _y, color) {

    override fun getWidth(): Int = radius * 2
    override fun getHeight(): Int = radius * 2

    override fun paint(graphics: Graphics) {
        super.paint(graphics)
        graphics.drawOval(_x, _y, getWidth() - 1, getHeight() - 1)
    }
}

private class Rectangle(
    val _x: Int,
    val _y: Int,
    val _width: Int,
    val _height: Int,
    color: Color
) : BaseShape(_x, _y, color) {

    override fun getHeight(): Int {
        return _height
    }

    override fun getWidth(): Int {
        return _width
    }

    override fun paint(graphics: Graphics) {
        super.paint(graphics)
        graphics.drawRect(_x, _y, getWidth() - 1, getHeight() - 1)
    }
}

private class CompoundShape(vararg components: Shape) : BaseShape(0, 0, Color.BLACK) {
    protected val children = mutableListOf<Shape>()

    init {
        add(*components)
    }

    fun add(component: Shape) {
        children.add(component)
    }

    fun add(vararg components: Shape) {
        children.addAll(components)
    }

    fun remove(component: Shape) {
        children.remove(component)
    }

    fun remove(vararg components: Shape) {
        children.removeAll(components)
    }

    fun clear() {
        children.clear()
    }

    override fun getX(): Int {
        if (children.isEmpty()) {
            return 0
        }
        var x = children[0].getX()
        children.forEach { child ->
            if (child.getX() < x) {
                x = child.getX()
            }
        }
        return x
    }

    override fun getY(): Int {
        if (children.isEmpty()) {
            return 0
        }
        var y = children[0].getY()
        children.forEach { child ->
            if (child.getY() < y) {
                y = child.getY()
            }
        }
        return y
    }

    override fun getWidth(): Int {
        var maxWidth = 0
        val x = getX()
        children.forEach { child ->
            val childsRelativeX = child.getX() - x
            val childWidth = childsRelativeX + child.getWidth()
            if (childWidth > maxWidth) {
                maxWidth = childWidth
            }
        }
        return maxWidth
    }

    override fun getHeight(): Int {
        var maxHeight = 0
        val y = getY()
        children.forEach { child ->
            val childsRelativeY = child.getY() - y
            val childHeight = childsRelativeY + child.getHeight()
            if (childHeight > maxHeight) {
                maxHeight = childHeight
            }
        }
        return maxHeight
    }

    override fun move(x: Int, y: Int) {
        children.forEach { child ->
            child.move(x, y)
        }
    }

    override fun isInsideBounds(x: Int, y: Int) = children.any { it.isInsideBounds(x, y) }

    override fun unselect() {
        super.unselect()
        children.forEach { child ->
            child.unselect()
        }
    }

    override fun paint(graphics: Graphics) {
        if (isSelected()) {
            enableSelectionStyle(graphics)
            graphics.drawRect(getX() - 1, getY() - 1, getWidth() + 1, getHeight() + 1)
            disableSelectionStyle(graphics)

            children.forEach { child ->
                child.paint(graphics)
            }
        }
    }

    fun selectChildAt(x: Int, y: Int): Boolean {
        children.forEach { child ->
            if (child.isInsideBounds(x, y)) {
                child.select()
                return true
            }
        }
        return false
    }
}

//endregion

//region editor

private class ImageEditor {
    private lateinit var canvas: EditorCanvas
    val allShapes = CompoundShape()

    fun loadShapes(vararg shape: Shape) {
        allShapes.clear()
        allShapes.add(*shape)
        canvas = EditorCanvas(allShapes)
        canvas.refresh()
    }

    private class EditorCanvas(val shapes: CompoundShape) : Canvas() {
        companion object {
            private const val PADDING = 10
        }

        private lateinit var frame: JFrame

        init {
            createFrame()
            refresh()
            addMouseListener(object : MouseAdapter() {
                override fun mousePressed(e: MouseEvent) {
                    shapes.unselect()
                    shapes.selectChildAt(e.x, e.y)
                    e.component.repaint()
                }
            })
        }

        fun createFrame() {
            frame = JFrame()
            frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
            frame.setLocationRelativeTo(null)

            val contentPanel = JPanel()
            val padding = BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING)
            contentPanel.border = padding
            frame.contentPane = contentPanel

            frame.add(this)
            frame.isVisible = true
            frame.contentPane.background = Color.LIGHT_GRAY
        }

        override fun getWidth(): Int {
            return shapes.getX() + shapes.getWidth() + PADDING
        }

        override fun getHeight(): Int {
            return shapes.getY() + shapes.getHeight() + PADDING
        }

        fun refresh() {
            this.setSize(width, height)
            frame.pack()
        }

        override fun paint(g: Graphics) {
            shapes.paint(g)
        }
    }
}

//endregion