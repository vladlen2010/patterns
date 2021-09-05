package patterns.builder

fun main() {
    val builder = CarBuilder()
    Director.constructSportsCar(builder)
    val car = builder.build()
    println("Car built:\n${car.carType}")

    val manualBuilder = CarManualBuilder()
    Director.constructSportsCar(manualBuilder)
    val manual = manualBuilder.build()
    println("\nCar manual built:\n${manual.print()}")
}

//region builders

private interface Builder {
    fun setCarType(carType: CarType)
    fun setSeats(seats: Int)
    fun setEngine(engine: Engine)
    fun setTransmission(transmission: Transmission)
    fun setTripComputer(tripComputer: TripComputer)
    fun setGPSNavigator(gpsNavigator: GPSNavigator)
}

private class CarBuilder : Builder {
    private lateinit var carType: CarType
    private var seats: Int = 0
    private lateinit var engine: Engine
    private lateinit var transmission: Transmission
    private var tripComputer: TripComputer? = null
    private var gpsNavigator: GPSNavigator? = null

    override fun setCarType(carType: CarType) {
        this.carType = carType
    }

    override fun setSeats(seats: Int) {
        this.seats = seats
    }

    override fun setEngine(engine: Engine) {
        this.engine = engine
    }

    override fun setTransmission(transmission: Transmission) {
        this.transmission = transmission
    }

    override fun setTripComputer(tripComputer: TripComputer) {
        this.tripComputer = tripComputer
    }

    override fun setGPSNavigator(gpsNavigator: GPSNavigator) {
        this.gpsNavigator = gpsNavigator
    }

    fun build(): Car = Car(carType, seats, engine, transmission, tripComputer, gpsNavigator)
}

private class CarManualBuilder : Builder {
    private lateinit var carType: CarType
    private var seats: Int = 0
    private lateinit var engine: Engine
    private lateinit var transmission: Transmission
    private var tripComputer: TripComputer? = null
    private var gpsNavigator: GPSNavigator? = null

    override fun setCarType(carType: CarType) {
        this.carType = carType
    }

    override fun setSeats(seats: Int) {
        this.seats = seats
    }

    override fun setEngine(engine: Engine) {
        this.engine = engine
    }

    override fun setTransmission(transmission: Transmission) {
        this.transmission = transmission
    }

    override fun setTripComputer(tripComputer: TripComputer) {
        this.tripComputer = tripComputer
    }

    override fun setGPSNavigator(gpsNavigator: GPSNavigator) {
        this.gpsNavigator = gpsNavigator
    }

    fun build(): Manual = Manual(carType, seats, engine, transmission, tripComputer, gpsNavigator)
}

//endregion

//region Cars

private data class Car(
    val carType: CarType,
    val seats: Int,
    val engine: Engine,
    val transmission: Transmission,
    val tripComputer: TripComputer?,
    val gpsNavigator: GPSNavigator?,
    var fuel: Double = 0.0
) {

    init {
        tripComputer?.setCar(this)
    }
}

private data class Manual(
    private val carType: CarType,
    private val seats: Int,
    private val engine: Engine,
    private val transmission: Transmission,
    private val tripComputer: TripComputer?,
    private val gpsNavigator: GPSNavigator?
) {

    fun print(): String {
        return buildString {
            appendLine("Type of car: $carType")
            appendLine("Count of seats: $seats")
            appendLine("Engine: volume - ${engine.volume}; mileage - ${engine.mileage}")
            appendLine("Transmission: $transmission")
            appendLine(tripComputer?.let { "Trip Computer: Functional" } ?: "Trip Computer: N/A")
            appendLine(gpsNavigator?.let { "GPS Navigator: Functional" } ?: "GPS Navigator: N/A")
        }
    }
}

private enum class CarType {
    CITY_CAR, SPORTS_CAR, SUV
}

//endregion

//region components

private data class Engine(
    val volume: Double,
    var mileage: Double,
    var isStarted: Boolean = false
) {
    fun on() {
        isStarted = true
    }

    fun off() {
        isStarted = false
    }

    fun go(mileage: Double) {
        if (isStarted) {
            this.mileage += mileage
        } else {
            System.err.println("Cannot go(), you must start engine first!")
        }
    }
}

private data class GPSNavigator(
    private var route: String = "221b, Baker Street, London  to Scotland Yard, 8-10 Broadway, London"
)

private enum class Transmission {
    SINGLE_SPEED, MANUAL, AUTOMATIC, SEMI_AUTOMATIC
}

private class TripComputer {
    private lateinit var car: Car

    fun setCar(car: Car) {
        this.car = car
    }

    fun showFuelLevel() = println("Fuel level: ${car.fuel}")

    fun showStatus() = println(if (car.engine.isStarted) "Car is started" else "Car isn't started")
}

//endregion

private object Director {
    fun constructSportsCar(builder: Builder) = with(builder) {
        setCarType(CarType.SPORTS_CAR)
        setSeats(2)
        setEngine(Engine(3.0, 0.0))
        setTransmission(Transmission.SEMI_AUTOMATIC)
        setTripComputer(TripComputer())
        setGPSNavigator(GPSNavigator())
    }

    fun constructCityCar(builder: Builder) = with(builder) {
        setCarType(CarType.CITY_CAR)
        setSeats(2)
        setEngine(Engine(1.2, 0.0))
        setTransmission(Transmission.AUTOMATIC)
        setTripComputer(TripComputer())
        setGPSNavigator(GPSNavigator())
    }

    fun constructSUV(builder: Builder) = with(builder) {
        setCarType(CarType.SUV)
        setSeats(4)
        setEngine(Engine(2.5, 0.0))
        setTransmission(Transmission.MANUAL)
        setGPSNavigator(GPSNavigator())
    }
}