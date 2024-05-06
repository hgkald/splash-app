package no.uio.ifi.in2000.team22.badeapp.model.swimspots

data class Swimspot(
    val id: Int,
    val name: String,
    val type: SwimspotType,
    val lon: Double,
    val lat: Double,
    val municipality: String,
    val county: String,
    val searchWords: List<String> = emptyList(),
    var distance: Float? = null
)