package no.uio.ifi.in2000.team22.badeapp.data.enTur


data class Root (
    val geoCoding: GeoCoding,
    val type: String,
    val features: List<Feature>,
    val bbox: List<Double>
)

data class GeoCoding(
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
    val layers : List<String>,
    val sources : List<String>,
    val size : Int,
    val private : Boolean,
    val point : Point,
    val boundary: Boundary,
    val querySize : Int
)

data class Point(
    val lat : Double,
    val lon : Double
)

data class Boundary (
    val circle: Circle
)

data class Circle(
    val lat : Double,
    val lon : Double,
    val radius : Int
)

data class Engine (
    val name : String,
    val author : String,
    val version : String
)

data class Geometry(
    val type : String,
    val coordinates: List<Double>
)

data class Properties(
    val gid : String,
    val layer : String,
    val source: String,
    val name : String,
    val confidence : Double,
    val distance : Double,
    val label : String,
    val category : List<String>
)


data class StopPlace(
    val lat: Double,
    val lon: Double,
    val name: String,
    val category: List<String>,
    val label : String
)
