package no.uio.ifi.in2000.team22.badeapp


import kotlinx.coroutines.runBlocking
import no.uio.ifi.in2000.team22.badeapp.data.oceanforecastApi.OceanforecastDataSource
import no.uio.ifi.in2000.team22.badeapp.data.oceanforecastApi.OceanforecastRepository
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Tests for Location Forecast API
 */
class OceanForecastUnitTest {

    private val OceanForRepo = OceanforecastRepository(OceanforecastDataSource())

    private var forecast = runBlocking { OceanForRepo.fetchTemperature(59.911491, 10.757933) }


    /**
     * Tests if the fetched data is not empty
     */
    @Test
    fun test_get_forecast_expected_true() {

        //Assert
        assertNotNull(forecast)
    }

    /**
     * Tests if the fetched list contains data
     */
    @Test
    fun test_get_forecast_data_expected_true() {

        //Arrange and act
        val time = forecast?.time
        //Assert
        println("Time: $time")
        assertNotNull(time)
    }


}