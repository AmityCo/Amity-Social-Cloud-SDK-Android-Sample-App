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
                "b3baba5c38d2f6334c61de1a5b0e1680d05b8fb0b93c3925"
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