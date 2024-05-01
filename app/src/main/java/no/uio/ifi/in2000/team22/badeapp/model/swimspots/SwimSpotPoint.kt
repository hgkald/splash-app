package no.uio.ifi.in2000.team22.badeapp.model.swimspots

import com.mapbox.geojson.BoundingBox
import com.mapbox.geojson.CoordinateContainer
import com.mapbox.geojson.GeoJson
import com.mapbox.geojson.Geometry
import com.mapbox.geojson.Point

class SwimSpotPoint(
    val swimSpot: Swimspot,
    private val point: Point
) : GeoJson, Geometry, CoordinateContainer<List<Double>> {

    override fun type(): String {
        return point.type()
    }

    override fun toJson(): String {
        return point.toJson()
    }

    override fun bbox(): BoundingBox? {
        return point.bbox()
    }

    override fun coordinates(): List<Double> {
        return point.coordinates()
    }
}