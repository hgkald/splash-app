package no.uio.ifi.in2000.team22.badeapp.model.swimspots

enum class SwimspotType(val norsk: String) {
    SALT("Saltvann"),
    FRESH("Ferskvann"),
    UNKNOWN("");


    companion object {
        /**
         * Creates an instance of [SwimspotType] from a string. If the string cannot be parsed
         * the SwimspotType is [UNKNOWN]
         *
         * @param type string containing "salt" og "fersk"
         *
         * @return An instance of [SwimspotType] corresponding with the given string
         */
        fun fromString(type: String): SwimspotType {
            return when (type.lowercase()) {
                "salt" -> SALT
                "fersk" -> FRESH
                else -> UNKNOWN
            }
        }
    }
}
