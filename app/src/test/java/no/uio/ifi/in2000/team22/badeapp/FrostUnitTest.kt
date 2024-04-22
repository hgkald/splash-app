//package no.uio.ifi.in2000.team22.badeapp
//
//import kotlinx.coroutines.runBlocking
//import no.uio.ifi.in2000.team22.badeapp.data.frostApi.FrostRepository
//import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot
//import org.junit.Assert.assertEquals
//import org.junit.Assert.assertNotEquals
//import org.junit.Assert.assertNotNull
//import org.junit.Test
//
///**
// * Tests for Frost API
// */
//class FrostUnitTest {
//    private val FrostRepo = FrostRepository()
//    var Temperature = runBlocking { FrostRepo.fetchWaterTemperature(lat = 50.00, lon = 10.00) }
//
//
//    /**
//     * Tests if the fetched list is not empty
//     */
//    @Test
//    fun test_get_Temperaature_expected_true() {
//
//        //Arrange and act
//        val empty = emptyList<Swimspot>()
//        //Assert
//        assertNotEquals(empty, swimSpots)
//    }
//
//    /**
//     * Tests if the fetched list contains data
//     */
//    @Test
//    fun test_get_swimSpots_data_expected_true() {
//
//        //Arrange and act
//        val first = swimSpots[0].name
//        //Assert
//        println("Name of first swimSpot: $first")
//        assertNotNull(first)
//    }
//
//    /**
//     * Tests if each swimSpot has an unique ID
//     */
//    @Test
//    fun test_swimSpots_have_unique_IDS_expected_true() {
//
//        //Arrange and act
//        val idList = mutableListOf<Int?>()
//        for (spots in swimSpots) {
//            idList.add(spots.id)
//        }
//        val idSet = idList.toSet()
//
//        //Assert
//        println("ID_Set size: ${idSet.size}")
//        println("swimSpots size: ${swimSpots.size}")
//        assertEquals(swimSpots.size, idSet.size)
//    }
//
//}