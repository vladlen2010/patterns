package patterns

import java.util.concurrent.atomic.AtomicInteger

fun main() {
    for (i in 1..10) println(CounterSingleton.increment())
}

object CounterSingleton {
    init {
        println("init singleton")
    }
    private val counter = AtomicInteger(0)
    fun increment() = counter.incrementAndGet()
}
