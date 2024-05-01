package no.uio.ifi.in2000.team22.badeapp

import kotlinx.coroutines.runBlocking
import no.uio.ifi.in2000.team22.badeapp.data.metalert.MetAlertRepository
import no.uio.ifi.in2000.team22.badeapp.model.alerts.Alert
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Tests for MetAlerts API
 */
class MetAlertsUnitTest {
    private val alertRepo = MetAlertRepository()
    var alerts = runBlocking { alertRepo.getAlertsForPosition(59.911491, 10.757933) }


    /**
     * Tests if the fetched list is not empty
     */
    @Test
    fun test_get_alerts_expected_true() {

        //Arrange and act
        val empty = emptyList<Alert>()
        //Assert
        assertNotEquals(empty, alerts)
    }

    /**
     * Tests if the fetched list contains data
     */
    @Test
    fun test_get_alerts_data_expected_true() {

        //Arrange and act
        val first = alerts[0].areaName
        //Assert
        println("Name of first alert area: ${alerts[0].areaName}")
        println("Number of alerts: ${alerts.size}")
        assertNotNull(first)
    }

    /**
     * Tests if alerts list if full of duplicates
     */
    @Test
    fun test_alerts_list_is_not_full_of_duplicates_expected_true() {

        //Arrange and act
        val alertOne = alerts[0]
        val alertsTwo = alerts[1]

        //Assert
        assertNotEquals(alertOne, alertsTwo)
    }

}