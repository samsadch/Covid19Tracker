package com.health.covid19tracker.utils

import android.content.Context
import android.net.ConnectivityManager


//Checking whether online or not passing context
internal fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}