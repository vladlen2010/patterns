package patterns.prototype

fun main() {
    val pcFromWarehouse = PC()

    val pwnerPC = pcFromWarehouse.copy(graphicCard = "nKFC 8999ZTXX",
        ram = "16GB BBR6")

    println(pwnerPC)
}


data class PC(val motherboard: String = "Terasus XZ27",
              val cpu: String = "Until Atom K500",
              val ram: String = "8GB Microcend BBR5",
              val graphicCard: String = "nKFC 8100TZ")
