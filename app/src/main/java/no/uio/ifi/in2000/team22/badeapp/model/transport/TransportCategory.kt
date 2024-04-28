package no.uio.ifi.in2000.team22.badeapp.model.transport

import no.uio.ifi.in2000.team22.badeapp.R

enum class TransportCategory(vararg category: String) {
    BUS("onstreetBus", "busStation", "coachStation"),
    TRAM("onstreetTram", "tramStation"),
    RAIL("railStation"),
    METRO("metroStation"),
    BOAT("harbourPort", "ferryPort", "ferryStop");

    companion object {
        fun toDrawable(category: TransportCategory): Int {
            return when (category) {
                BUS -> R.drawable.transport_category_bus_24
                TRAM -> R.drawable.transport_category_tram_24
                RAIL -> R.drawable.transport_category_train_24
                METRO -> R.drawable.transport_category_metro_24
                BOAT -> R.drawable.transport_category_boat_24
            }
        }

        fun fromString(category: String): TransportCategory? {
            return when (category) {
                "onstreetBus", "busStation", "coachStation" -> BUS
                "onstreetTram", "tramStation" -> TRAM
                "railStation" -> RAIL
                "metroStation" -> METRO
                "harbourPort", "ferryPort", "ferryStop" -> BOAT
                else -> null
            }
        }

        fun toNorwegian(category: TransportCategory): String {
            return when (category) {
                BUS -> "Buss"
                TRAM -> "Trikk"
                RAIL -> "Tog"
                METRO -> "T-bane"
                BOAT -> "Ferje/Båt"
            }
        }
    }
}