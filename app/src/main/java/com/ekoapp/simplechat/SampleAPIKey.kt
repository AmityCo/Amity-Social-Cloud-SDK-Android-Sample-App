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
                "b3baee0938dff8344a618b4d030d1688d10b85eabc646624"
            }
            production -> {
                "b3bee90c39d9a5644831d84e5a0d1688d100ddebef3c6e78"
            }
            else -> {
                ""
            }
        }
    }

}