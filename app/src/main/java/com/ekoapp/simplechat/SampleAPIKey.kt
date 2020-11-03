package com.ekoapp.simplechat

object SampleAPIKey {

    const val preprod = "preprod"
    const val production = "production"

    fun get(): String {
        return get(SampleApp.get().getString(R.string.sdk_environment))
    }

    private fun get(environment: String): String {
        return when (environment) {
            preprod -> {
                "b3bab95b3edbf9661a368518045b4481d35cdfeaec35677d"
            }
            production -> {
                ""
            }
            else -> {
                ""
            }
        }
    }

}