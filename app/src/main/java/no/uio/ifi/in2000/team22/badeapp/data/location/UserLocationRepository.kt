package no.uio.ifi.in2000.team22.badeapp.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LocationState(
    val lastKnownLocation: Location? = null,
    val permissionGranted: Boolean = false
)

class UserLocationRepository(
    private val context: Context
) {
    private val locationClient =
        LocationServices.getFusedLocationProviderClient(context)

    private var _locationState = MutableStateFlow(LocationState())

    //This callback handles new location results.
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            val newLocation = p0.lastLocation
            val oldLocation = _locationState.value.lastKnownLocation

            val updateLocation = {
                Log.i("UserLocationRepo", "Updating current location")
                _locationState.update {
                    it.copy(
                        lastKnownLocation = p0.lastLocation
                    )
                }
            }

            //If the new location is null, then ignore the new result
            if (oldLocation != null && newLocation == null) {
                return
            }

            //If the old location is null, then update the location
            if (oldLocation == null) {
                updateLocation()
            }

            /*
            Only update location if both is not null, and the distance between
            the points is more then 200 meters.
            */
            if (oldLocation != null &&
                newLocation != null &&
                oldLocation.distanceTo(newLocation) > 200
            ) {
                updateLocation()
            }

        }
    }

    /**
     * Returns a stateflow of the users given location
     */
    fun observe(): StateFlow<LocationState> = _locationState.asStateFlow()

    /**
     * Checks if permissions for location has been granted
     *
     * @return [true] if permissions has been granted, or [false] if not granted.
     */
    fun checkPermissions(): Boolean {
        val fineLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        _locationState.update {
            it.copy(
                permissionGranted = (fineLocation || coarseLocation)
            )
        }

        Log.i("UserLocationRepo", "Current permission state is ${fineLocation || coarseLocation}")

        return (fineLocation || coarseLocation)
    }

    /**
     * This starts the location updates, and fetches a new location every 5 seconds.
     */
    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_LOW_POWER,
            5_000
        ).build()

        if (checkPermissions() == true) {
            locationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    /**
     * This stops the location updates
     */
    fun stopLocationUpdates() {
        locationClient.removeLocationUpdates(locationCallback)
    }

    init {
        checkPermissions()
    }
}