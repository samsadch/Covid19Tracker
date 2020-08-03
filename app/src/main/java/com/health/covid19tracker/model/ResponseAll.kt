package com.health.covid19tracker.model

data class ResponseAll(
    val cases: Int,
    val deaths: Int,
    val recovered: Int,
    val updated: Long
)