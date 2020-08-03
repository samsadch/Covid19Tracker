package com.health.covid19tracker.model

data class ResponseCountries(
    val active: Int,
    val cases: Int,
    val casesPerOneMillion: Int,
    val country: String,
    val critical: Int,
    val deaths: Int,
    val recovered: Int,
    val todayCases: Int,
    val todayDeaths: Int,
    val activePerOneMillion: Double?,
    val continent: String?,
    val countryInfo: CountryInfo?,
    val criticalPerOneMillion: Double?,
    val deathsPerOneMillion: Double?,
    val oneCasePerPeople: Double?,
    val oneDeathPerPeople: Double?,
    val oneTestPerPeople: Double?,
    val population: Int?,
    val recoveredPerOneMillion: Double?,
    val tests: Int?,
    val testsPerOneMillion: Double?,
    val todayRecovered: Int?,
    val updated: Long?
)
