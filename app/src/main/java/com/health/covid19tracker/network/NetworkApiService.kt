package com.health.covid19tracker.network

import android.content.Context
import com.google.gson.GsonBuilder
import com.health.covid19tracker.R
import com.health.covid19tracker.model.ResponseAll
import com.health.covid19tracker.model.ResponseCountries
import com.health.covid19tracker.model.ResponseCountryDetail
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*
import java.util.concurrent.TimeUnit

interface NetworkApiService {

    //Get statistics
    @GET("all")
    fun getAll(): Call<ResponseAll>

    //Get countries data
    @GET("countries")
    fun getCountries(): Call<List<ResponseCountries>>

    //Get particular country data
    @GET("countries/{query}")
    fun getCountry(@Path("query") query: String): Call<ResponseCountryDetail>

}

object Api {

    fun createWithHeader(context: Context): NetworkApiService {
        val specs = listOf(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS)
        val gson = GsonBuilder().setLenient().create()
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .build()
            chain.proceed(newRequest)
        }.connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .connectionSpecs(
                Arrays.asList(
                    ConnectionSpec.MODERN_TLS,
                    ConnectionSpec.COMPATIBLE_TLS,
                    ConnectionSpec.CLEARTEXT
                )
            )
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(context.getString(R.string.url)).client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(NetworkApiService::class.java)
    }
}