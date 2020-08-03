package com.health.covid19tracker.model


data class CountryInfo(
    val _id: Int,
    val flag: String?,
    val iso2: String?,
    val iso3: String?,
    val lat: Double?,
    val long: Double?
)