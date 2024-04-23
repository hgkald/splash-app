package no.uio.ifi.in2000.team22.badeapp.data.location

import android.util.Log
import com.mapbox.common.location.AccuracyLevel
import com.mapbox.common.location.DeviceLocationProvider
import com.mapbox.common.location.IntervalSettings
import com.mapbox.common.location.Location
import com.mapbox.common.location.LocationProviderRequest
import com.mapbox.common.location.LocationService
import com.mapbox.common.location.LocationServiceFactory

class UserLocationRepository {
    private val locationService: LocationService = LocationServiceFactory.getOrCreate()
    private var lastLocation: Location? = null

    /*
    init {
        fetchLastKnownLocation()
    }

    private fun fetchLastKnownLocation() {
        Log.i("LocationRepository", "Fetching last known location")
        var locationProvider: DeviceLocationProvider? = null
        val request = LocationProviderRequest.Builder()
            .interval(IntervalSettings.Builder().interval(0L).minimumInterval(0L).maximumInterval(0L).build())
            .displacement(0F)
            .accuracy(AccuracyLevel.HIGHEST)
            .build()

        val result = locationService.getDeviceLocationProvider(request)
        if (result.isValue) {
            locationProvider = result.value!!

            val lastLocationCancelable = locationProvider.getLastLocation { loc ->
                loc?.let {
                    Log.i("UserLocationRepository", "Last location received: $loc")
                    this.lastLocation = loc
                }
            }
        } else {
            Log.e("LocationRepository", "Failed to get device location provider")
        }
    }

    fun getLastKnownLocation(): Location? {
        fetchLastKnownLocation()
        return this.lastLocation
    }*/
}