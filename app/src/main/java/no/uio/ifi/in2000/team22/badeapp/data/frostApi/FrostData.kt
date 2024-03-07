package no.uio.ifi.in2000.team22.badeapp.data.frostApi


data class Badested(
    val name: String,
    val lon: Double,
    val lat: Double
)
data class Data (
    val tseries : List<Tseries>
)

data class Tseries (
    val header : Header
)

data class Header (
    val id : Id,
    val extra : Extra
)

data class Id (
    val buoyid : String,
    val parameter : String,
    val source : String
)

data class Extra (
    val name : String,
    val pos : Pos
)


data class Pos (
    val lat : String,
    val lon : String
)


data class FrostData (
    val data : Data
)
