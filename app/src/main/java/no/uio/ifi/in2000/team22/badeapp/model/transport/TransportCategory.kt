package no.uio.ifi.in2000.team22.badeapp.model.transport

import no.uio.ifi.in2000.team22.badeapp.R

enum class TransportCategory {
    BUS,
    TRAM,
    RAIL,
    METRO,
    BOAT;

    companion object {

        /**
         * Converts a [TransportCategory] to the corresponding drawable
         */
        fun toDrawable(category: TransportCategory): Int {
            return when (category) {
                BUS -> R.drawable.transport_category_bus_24
                TRAM -> R.drawable.transport_category_tram_24
                RAIL -> R.drawable.transport_category_train_24
                METRO -> R.drawable.transport_category_metro_24
                BOAT -> R.drawable.transport_category_boat_24
            }
        }

        /**
         * Converts a string into the corresponding [TransportCategory].
         *
         * @param category a string from the EnTur API.
         *
         * @return An instance of [TransportCategory], or null if the given string does not
         * match any transport category.
         */
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

        /**
         * Converts a [TransportCategory] to a string in norwegian
         *
         * @return The corresponding string in norwegian.
         */
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