package no.uio.ifi.in2000.team22.badeapp.data

data class Root (
    val genCoding: GenCoding,
    val type: String,
    val features: Feature,
    val bbox: List<Double>
)

data class GenCoding(
    val version: String,
    val attribution: String,
    val query : Query,
    val engine : Engine,
    val timestamp: Int
)

data class Feature(
    val type : String,
    val geometry : Geometry,
    val properties : Properties
)

data class Query(
    val layers : List<String>, // annerledes på bruno/ postman og nettsiden
    val sources : List<String>,
    val size : Int,
    val lat : Double,
    val lon : Double,
    val boundary_circle_radius : Int, // ??
    val boundary_circle_lat : Double,
    val boundary_circle_lon : Double,
    val querySize : Int
)

data class Engine (
    val name : String,
    val author : String,
    val version : String
)

data class Geometry(
    val type : String,
    val coordinates: List<Double> // SAMME som oppe ^^
)

data class Properties(
    val gid : String,
    val layer : String,
    val confidence : Double,
    val distance : Double,

    )
