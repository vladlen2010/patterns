package patterns.prototype

import java.util.*

private fun main() {
    val shapes = mutableListOf<Shape>()
    val shapesCopy = mutableListOf<Shape>()

    val circle = Circle()
    with(circle) {
        x = 10
        y = 20
        radius = 15
        color = "red"
    }
    shapes.add(circle)

    val anotherCircle = circle.clone()
    shapes.add(anotherCircle)

    val rectangle = Rectangle()
    with(rectangle) {
        width = 10
        height = 20
        color = "blue"
    }
    shapes.add(rectangle)

    Shape.cloneAndCompare(shapes, shapesCopy)


    println("________________________________________")

    val cache = BundledShapeCache()

    val shape1 = cache.get("Big green circle")
    val shape2 = cache.get("Medium blue rectangle")
    val shape3 = cache.get("Medium blue rectangle")

    if (shape1 !== shape2 && shape1 != shape2) {
        println("Big green circle != Medium blue rectangle (yay!)")
    } else {
        println("Big green circle == Medium blue rectangle (booo!)")
    }

    if (shape2 !== shape3) {
        println("Medium blue rectangles are two different objects (yay!)")
        if (shape2 == shape3) {
            println("And they are identical (yay!)")
        } else {
            println("But they are not identical (booo!)")
        }
    } else {
        println("Rectangle objects are the same (booo!)")
    }
}


//region shapes

abstract class Shape(
    private val target: Shape?
) {

    companion object {
        fun cloneAndCompare(
            shapes: List<Shape>,
            shapesCopy: MutableList<Shape>
        ) {
            shapes.forEach {
                shapesCopy.add(it.clone())
            }

            for (i in shapes.indices) {
                if (shapes[i] !== shapesCopy[i]) {
                    println("$i : Shapes are different objects (yay!)")
                    if (shapes[i] == shapesCopy[i]) {
                        println("$i : And they are identical (yay!)")
                    } else {
                        println("$i : But they are not identical (booo!)")
                    }
                } else {
                    println("$i : Shape objects are the same (booo!)")
                }
            }
        }
    }

    var x: Int? = null
    var y: Int? = null
    var color: String? = null

    init {
        target?.let {
            x = it.x
            y = it.y
            color = it.color
        }
    }

    abstract fun clone(): Shape

    override fun equals(other: Any?): Boolean {
        if (other !is Shape) return false
        return other.x == x && other.y == y && Objects.equals(other.color, color)
    }

    override fun hashCode(): Int {
        var result = target?.hashCode() ?: 0
        result = 31 * result + (x ?: 0)
        result = 31 * result + (y ?: 0)
        result = 31 * result + (color?.hashCode() ?: 0)
        return result
    }
}

private class Circle(
    private val target: Circle? = null
) : Shape(target) {

    var radius: Int? = null

    init {
        target?.let {
            radius = it.radius
        }
    }

    override fun clone(): Shape = Circle(this)

    override fun equals(other: Any?): Boolean {
        if (other !is Circle || !super.equals(other)) return false
        return other.radius == radius
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (target?.hashCode() ?: 0)
        result = 31 * result + (radius ?: 0)
        return result
    }
}

private class Rectangle(
    private val target: Rectangle? = null
) : Shape(target) {

    var width: Int? = null
    var height: Int? = null

    init {
        target?.let {
            width = it.width
            height = it.height
        }
    }

    override fun clone(): Shape = Rectangle(this)

    override fun equals(other: Any?): Boolean {
        if (other !is Rectangle || !super.equals(other)) return false
        return other.width == width && other.height == height
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (target?.hashCode() ?: 0)
        result = 31 * result + (width ?: 0)
        result = 31 * result + (height ?: 0)
        return result
    }
}

//endregion

//region cache

private class BundledShapeCache {

    private val cache = mutableMapOf<String, Shape>()

    init {
        val circle = Circle()
        with(circle) {
            x = 5
            y = 7
            radius = 45
            color = "Green"
        }

        val rectangle = Rectangle()
        with(rectangle) {
            x = 6
            y = 9
            width = 8
            height = 10
            color = "Blue"
        }

        cache["Big green circle"] = circle
        cache["Medium blue rectangle"] = rectangle
    }

    fun put(key: String, shape: Shape): Shape {
        cache[key] = shape
        return shape
    }

    fun get(key: String) = cache[key]?.clone()
}