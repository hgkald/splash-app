package no.uio.ifi.in2000.team22.badeapp


import kotlinx.coroutines.runBlocking
import no.uio.ifi.in2000.team22.badeapp.data.locationforecastApi.LocationforecastDataSource
import no.uio.ifi.in2000.team22.badeapp.data.locationforecastApi.LocationforecastRepository
import org.junit.Test
import org.junit.Assert.*

/**
 * Tests for Location Forecast API
 */
class LocForecastUnitTest {

    private val locForRepo = LocationforecastRepository(LocationforecastDataSource())

    private var forecast = runBlocking { locForRepo.fetchCurrentWeather(59.911491, 10.757933) }


    /**
     * Tests if the fetched data is not empty
     */
    @Test
    fun test_get_forecast_expected_true(){

        //Assert
        assertNotNull(forecast)
    }
    /**
     * Tests if the fetched list contains data
     */
    @Test
    fun test_get_forecast_data_expected_true(){

        //Arrange and act
        val time = forecast.time
        //Assert
        println("Time: $time")
        assertNotNull(time)
    }



}