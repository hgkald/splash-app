package no.uio.ifi.in2000.team22.badeapp.model.uv

import androidx.compose.ui.graphics.Color
import kotlin.math.roundToInt


/**
 * Enum for representing UV
 *
 * Use [doubleToUV] to convert [Double] to instance of [UV]
 */
enum class UV {
    LOW,
    MEDIUM,
    HIGH,
    VERY_HIGH,
    UNKNOWN
}

/**
 * A function for convert doubles to UV.
 * Converts [Double] to nearest [Int] value, then to instance of [UV]
 *
 * @property uvIndex the value that will be converted
 * @return An instance of UV
 * @see UV
 */
fun doubleToUV(uvIndex: Double): UV {
    return when (uvIndex.roundToInt()) {
        in 0..2 -> UV.LOW
        in 3..5 -> UV.MEDIUM
        in 6..10 -> UV.HIGH
        else -> UV.VERY_HIGH
    }
}

/**
 * Converts a valid [UV] entity to a [Color]
 *
 * @property uv the UV entity that will be converted to a Color
 * @return An instance of Color
 * @see Color
 */
//fun uvToColor(uv: UV): Color {
//    return when (uv) {
//        UV.LOW -> Color("#94d171".toColorInt())
//        UV.MEDIUM -> Color("#ebd05b".toColorInt())
//        UV.HIGH -> Color("#cf3232".toColorInt())
//        UV.VERY_HIGH -> Color("#8c63b8".toColorInt())
//        UV.UNKNOWN -> Color.LightGray
//    }
//}

fun uvToNorwegian(uv: UV): String {
    return when (uv) {
        UV.LOW -> "Lavt"
        UV.MEDIUM -> "Moderat"
        UV.HIGH -> "Høyt"
        UV.VERY_HIGH -> "Veldig høyt"
        UV.UNKNOWN -> "Ukjent"
    }
}