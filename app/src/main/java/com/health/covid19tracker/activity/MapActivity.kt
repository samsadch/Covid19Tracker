package com.health.covid19tracker.activity

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.health.covid19tracker.R
import com.health.covid19tracker.model.ResponseCountries
import com.health.covid19tracker.network.Api
import com.health.covid19tracker.utils.Utils
import com.health.covid19tracker.utils.isOnline
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mapFragment: SupportMapFragment? = null
    private lateinit var context: Context
    var isOnline = true
    private var countriesList = ArrayList<ResponseCountries>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        context = this@MapActivity
        mapFragment = supportFragmentManager.findFragmentById(R.id.map2) as SupportMapFragment?
        //Initialise the map
        mapFragment!!.getMapAsync(this)

        //The function used to get the country details from the API
        getCountries()
    }


    private fun getCountries() {
        try {
            if (!isOnline(context)) {
                isOnline = false
                showSnackBar(getString(R.string.no_internet))
            } else {
                isOnline = true
                Utils.setLoading(context)
                Utils.showProgress()
                val service = Api.createWithHeader(context)
                val call = service.getCountries()
                call.enqueue(object : Callback<List<ResponseCountries>> {
                    override fun onResponse(
                        call: Call<List<ResponseCountries>>,
                        response: Response<List<ResponseCountries>>
                    ) {
                        Utils.hideProgress()
                        try {
                            val stat = response.body()
                            stat?.let {
                                for (item in stat) {
                                    countriesList.add(item)
                                }
                            }

                            mapFragment!!.getMapAsync(this@MapActivity)

                        } catch (e: Exception) {
                            //
                            Utils.hideProgress()
                            e.printStackTrace()
                            Utils.showIosDialog(context, e.message)
                        }
                    }

                    override fun onFailure(call: Call<List<ResponseCountries>>, t: Throwable) {
                        //Failed to load the Map
                        Utils.hideProgress()
                        Utils.showIosDialog(context, t.message)
                    }
                })
            }
        } catch (e: Exception) {
            //Exception while loading country data
            Utils.hideProgress()
            e.printStackTrace()
            Utils.showIosDialog(context, e.message)
        }
    }

    override fun onResume() {
        super.onResume()
        //On refreshing the app load the data again
        Utils.setLoading(context, "Refreshing")
        getCountries()
    }

    private fun showSnackBar(message: String) {
        val sb = Snackbar.make(
            findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_INDEFINITE
        )

        val snackbarView = sb.view
        snackbarView.setBackgroundColor(resources.getColor(R.color.color_button))
        val textView = snackbarView.findViewById(R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        sb.setAction("") {
            sb.dismiss()
        }
        sb.show()
    }

    override fun onMapReady(p0: GoogleMap?) {
        //On map ready call back check if the country list is empty or not
        //If list has values load that values in the mMap
        if (countriesList.size > 0) {
            loadMap(p0)
        }

    }


    //Loading Each location within the Map

    private fun loadMap(googleMap: GoogleMap?) {
        googleMap?.let {
            for (item in countriesList) {
                item.countryInfo?.let {
                    it.lat?.let { latitude ->
                        it.long?.let { longitude ->
                            val sydney = LatLng(latitude, longitude)
                            googleMap.uiSettings.isMyLocationButtonEnabled = true
                            googleMap.uiSettings.isMyLocationButtonEnabled = true
                            googleMap.addMarker(
                                MarkerOptions()
                                    .position(sydney)
                                    .title(item?.country + "  " + item?.todayCases + " cases reported Today")
                            )
                            //if you want to set the map to show user current location or
                            // particular location use belo lines of code
                            //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13f))
                        }
                    }

                }

            }
        }

    }
}