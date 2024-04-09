package no.uio.ifi.in2000.team22.badeapp.data

object MetAPI {
    const val HOST: String = "gw-uio.intark.uh-it.no"
    const val API_KEY_NAME: String = "X-Gravitee-API-Key"
    const val API_KEY: String = "1257d958-d771-4767-abb0-9d78c0f45025"

    object LocationForecast {
        const val PATH: String = "in2000/weatherapi/locationforecast/2.0/"
        const val TYPE: String = "complete"
    }

    object MetAlerts {
        const val PATH: String = "in2000/weatherapi/metalerts/2.0/"
        const val CURRENT_ENDPOINT = "current.json"
        const val TEST_ENDPOINT = "test.json"
    }
    
    /**
     * Object for OceanForecast API path
     */

    object OceanForecast {
        const val PATH: String = "in2000/weatherapi/oceanforecast/2.0/"
        const val TYPE: String = "complete"
    }
}
