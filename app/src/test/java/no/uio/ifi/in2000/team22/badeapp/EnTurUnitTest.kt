package no.uio.ifi.in2000.team22.badeapp


import kotlinx.coroutines.runBlocking
import no.uio.ifi.in2000.team22.badeapp.data.enTur.EnTurRepository
import no.uio.ifi.in2000.team22.badeapp.data.enTur.StopPlace
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Tests for EnTur API
 */
class EnTurUnitTest {

    private val enTurRepo = EnTurRepository()
    private var stopPlaces = runBlocking { enTurRepo.getStops(59.911491, 10.757933, 10, 10) }


    /**
     * Tests if the fetched list is not empty for Oslo
     */
    @Test
    fun test_get_stops_expected_true() {

        //Arrange and act
        val empty = emptyList<StopPlace>()
        //Assert
        assertNotEquals(empty, stopPlaces)
    }

    /**
     * Tests if the fetched list contains data
     */
    @Test
    fun test_get_stops_data_expected_true() {

        //Arrange and act
        val first = stopPlaces[0].name
        //Assert
        println("Name of first stop: $first")
        assertNotNull(first)
    }

    /**
     * Tests if StopPlace.type list is empty
     */
    @Test
    fun test_stopPlace_type_expected_true() {

        //Arrange and act
        val types = mutableListOf<String>()
        for (categories in stopPlaces[0].category) {
            types.add(categories)
        }

        //Assert
        println("Category list: $types")
        assertNotNull(stopPlaces[0].category[0])
    }


}