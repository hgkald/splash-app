package no.uio.ifi.in2000.team22.badeapp

import kotlinx.coroutines.runBlocking
import no.uio.ifi.in2000.team22.badeapp.data.frostApi.FrostRepository
import no.uio.ifi.in2000.team22.badeapp.model.forecast.WaterTemperature
import no.uio.ifi.in2000.team22.badeapp.utils.Common
import org.junit.BeforeClass
import org.junit.Test
import java.time.Instant

class FrostApiTest {
    companion object {
        val fromString = "2023-05-01T00:00:00Z"
        val toString = "2023-08-02T23:59:59Z"
        var validWaterTemp: WaterTemperature? = null
        var invalidWaterTemp: WaterTemperature? = null

        @JvmStatic
        @BeforeClass
        fun setup(): Unit {
            val frostRepo = FrostRepository();
            validWaterTemp =
                runBlocking {
                    frostRepo.fetchWaterTemperatureWithTime(
                        lat = Common.LATITUDE,
                        lon = Common.LONGITUDE,
                        from = Instant.parse(fromString),
                        to = Instant.parse(toString)
                    )
                }
            invalidWaterTemp = runBlocking {
                frostRepo.fetchWaterTemperature(-1.0, 0.0)
            }
        }
    }

    @Test
    fun testValidWaterTempNotNull() {
        assert(validWaterTemp != null)
//        println(validWaterTemp)
    }

    @Test
    fun testInvalidWaterTempIsNull() {
        assert(invalidWaterTemp == null)
//        println(invalidWaterTemp)
    }

    @Test
    fun testTimestampIsBeforeDateTrue() {
        //Arrange
        val before = Instant.parse(toString)

        //Act
        val result = validWaterTemp?.time?.isBefore(before)

        //Assert
        assert(result == true)
    }

    @Test
    fun testTimestampIsAfterDateTrue() {
        //Arrange
        val after = Instant.parse(fromString)

        //Act
        val result = validWaterTemp?.time?.isAfter(after)

        //Assert
        assert(result == true)
    }

    @Test
    fun testTimestampIsInstantTrue() {
        assert(validWaterTemp?.time is Instant)
    }


}