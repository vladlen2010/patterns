package patterns.static_factory_method

fun main() {
    println(NumberMaster.valueOf("123"))

    val instance = MyClass.create()
    println(instance)
}

class NumberMaster {
    companion object Parser {
        fun valueOf(numberStr: String): Long {
            return numberStr.toLong()
        }
    }
}

private class MyClass private constructor() {
    companion object {
        fun create() = MyClass()
    }
}