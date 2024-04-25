package no.uio.ifi.in2000.team22.badeapp.data.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LocationState(
    val lastKnownLocation: Location? = null,
    val permissionGranted: Boolean = false
)

class UserLocationRepository(
    val context: Context
) {
    val locationClient =
        LocationServices.getFusedLocationProviderClient(context)
    var lastKnownLocation: Location? = null;

    private var _locationState = MutableStateFlow(LocationState())

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

        return (fineLocation || coarseLocation)
    }

    init {
        checkPermissions()
    }
}