package no.uio.ifi.in2000.team22.badeapp.data

/**
 * Returns double value truncated to a certain number of decimal places
 * @return [Double]
 * @property [decimals] Number of decimal places
 */
fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return kotlin.math.round(this * multiplier) / multiplier
}
