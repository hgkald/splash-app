package no.uio.ifi.in2000.team22.badeapp

import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import no.uio.ifi.in2000.team22.badeapp.data.swimspots.SwimspotsRepository
import org.junit.BeforeClass
import org.junit.Test

class SwimspotsTest {
    companion object {
        lateinit var swimspotsRepo: SwimspotsRepository

        @JvmStatic
        @BeforeClass
        fun setup() {
            swimspotsRepo =
                SwimspotsRepository(InstrumentationRegistry.getInstrumentation().targetContext)
        }
    }

    @Test
    fun testSwimspotListEmptyIsFalse() {
        //Arrange
        val swimspots = runBlocking { swimspotsRepo.getAllSwimspots() }

        //Act
        val result = swimspots.isEmpty()

        //Assert
        assert(!result)
    }

    @Test
    fun testFetchSwimspotByIdIsTrue() {
        //Arrange
        val swimspot = runBlocking { swimspotsRepo.getSwimspotById("1") }

        //Act
        val result = swimspot != null

        //Assert
        assert(result)
    }

    @Test
    fun testFetchSwimspotByIdIsFalse() {
        //Arrange
        val swimspot = runBlocking { swimspotsRepo.getSwimspotById("11241341432") }

        //Act
        val result = swimspot != null

        //Assert
        assert(!result)
    }
}