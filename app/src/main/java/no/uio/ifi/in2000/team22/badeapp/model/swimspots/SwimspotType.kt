package no.uio.ifi.in2000.team22.badeapp.model.swimspots

enum class SwimspotType(type: String) {
    SALT("Saltvann"),
    FRESH("Ferskvann"),
    UNKNOWN("");


    companion object {
        fun fromString(type: String): SwimspotType {
            return when (type.lowercase()) {
                "salt" -> SALT
                "fersk" -> FRESH
                else -> UNKNOWN
            }
        }
    }
}
