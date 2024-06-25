package no.uio.ifi.in2000.team22.badeapp.data.metalert

import no.uio.ifi.in2000.team22.badeapp.model.alerts.Alert


class MetAlertRepository(
    private val metAlertDataSource: MetAlertDataSource = MetAlertDataSource()
) {
    /**
     * Returns a list of alerts from [MetAlertDataSource]
     */
    suspend fun getAlertList(): List<Alert> {
        return metAlertDataSource.fetchAlerts()
    }

    /**
     * Returns a the timestamp of the last change to the API.
     * timestamp format: 2024-03-12T09:53:35+00:00
     */
    suspend fun getLastChange(): String {
        return metAlertDataSource.fetchLastChanged()
    }

    /**
     * Check if a given point is inside the alert area
     *
     * @param lat latitude
     * @param long longitude
     *
     * @return A [Boolean] based on if  the point is inside the area or not
     */
    suspend fun isPointInAlertArea(lat: Double, long: Double): Boolean {
        val alerts = metAlertDataSource.fetchAlertsForPosition(lat, long)
        return alerts.isNotEmpty()
    }

    /**
     * Returns a list with lists of coordinates where in the inner most lists index 0 is longtitude
     * and index 1 is latitude. Each list of tuples is a polygon. The space within the polygon is the
     * alerted area.
     */
    suspend fun getAlertAreaCoords(): List<List<List<Double>>> {
        val info = getAlertList()
        var list: List<List<List<Double>>> = emptyList()
        info.forEach {
            list += it.geographicArea
        }
        return list
    }

    /**
     * Returns a list of all the alerts for a given point.
     *
     * @param lat latitude
     * @param long longitude
     *
     * @return A list of [Alert] with the current alerts.
     */
    suspend fun getAlertsForPosition(lat: Double, lon: Double): List<Alert> {
        return metAlertDataSource.fetchAlertsForPosition(lat, lon)
    }
}


// Main function for testing the code along the way
suspend fun main() {
    val rep = MetAlertRepository()
    rep.getAlertList().forEach {
        println(it.areaName)
        println(it.description)
        println(it.geographicArea)
        println(it.riskMatrixColor)
        println()
    }
    println()
    println(rep.getAlertAreaCoords())
    println()
    println(rep.isPointInAlertArea(60.6052, 5.2564))
    println(rep.isPointInAlertArea(63.5463, 7.7816))
    println(rep.isPointInAlertArea(59.2465, 5.1938))
    println(rep.getLastChange())
    println(rep.getAlertList()[0].timeFrame)
}


