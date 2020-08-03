package com.health.covid19tracker.activity

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.health.covid19tracker.R
import com.health.covid19tracker.adapter.CountriesAdapter
import com.health.covid19tracker.model.ResponseAll
import com.health.covid19tracker.model.ResponseCountries
import com.health.covid19tracker.network.Api
import com.health.covid19tracker.utils.Utils
import com.health.covid19tracker.utils.isOnline
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private lateinit var context: Context
    var isOnline = true
    private var countriesList = ArrayList<ResponseCountries>()

    private lateinit var adapter: CountriesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this@MainActivity
        setContentView(R.layout.activity_main)
        Utils.setLoading(context, "Loading")
        //Get the data using net work call in following function
        getALlData()
        getCountries()

        refreshImv.setOnClickListener {
            Utils.setLoading(context, "Refreshing")
            getALlData()
            getCountries()
        }

        updateListRcv.layoutManager = LinearLayoutManager(context)
        adapter = CountriesAdapter(context, countriesList)
        updateListRcv.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        Utils.setLoading(context, "Refreshing")
        getALlData()
        getCountries()
    }


    private fun getALlData() {
        try {
            if (!isOnline(context)) {
                isOnline = false
                showSnackBar("Internet Connection not available")
            } else {
                isOnline = true
                Utils.showProgress()
                val service = Api.createWithHeader(context)
                val call = service.getAll()
                call.enqueue(object : Callback<ResponseAll> {
                    override fun onResponse(
                        call: Call<ResponseAll>,
                        response: Response<ResponseAll>
                    ) {
                        Utils.hideProgress()
                        try {
                            val stat = response.body()
                            casesTxv.text = stat?.cases.toString()
                            deathsTxv.text = stat?.deaths.toString()
                            recoveredTxv.text = stat?.recovered.toString()
                            //lastUpdatedTxv
                            val calander1 = Calendar.getInstance(Locale.getDefault())
                            calander1.timeInMillis = stat!!.updated
                            val dateDisplay =
                                android.text.format.DateFormat.format("dd MMM hh:mm:aa", calander1)
                            lastUpdatedTxv.text =
                                resources.getString(R.string.last_updated) + dateDisplay
                        } catch (e: Exception) {
                            Utils.hideProgress()
                            e.printStackTrace()
                            Utils.showToast(context, e.message)
                        }
                    }

                    override fun onFailure(call: Call<ResponseAll>, t: Throwable) {
                        Utils.hideProgress()
                        Utils.showToast(context, t.message)
                    }
                })
            }
        } catch (e: Exception) {
            Utils.hideProgress()
            e.printStackTrace()
            Utils.showToast(context, e.message)
        }
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
                            if (stat != null) {
                                for (country in stat) {
                                    countriesList.add(country)
                                }
                                countriesList.also { its ->
                                    its.sortByDescending {
                                        it.cases
                                    }
                                }
                                removeDuplications()
                                adapter.notifyDataSetChanged()
                            }

                        } catch (e: Exception) {
                            Utils.hideProgress()
                            e.printStackTrace()
                            Utils.showToast(context, e.message)
                        }
                    }

                    override fun onFailure(call: Call<List<ResponseCountries>>, t: Throwable) {
                        Utils.hideProgress()
                        Utils.showToast(context, t.message)
                    }
                })
            }
        } catch (e: Exception) {
            Utils.hideProgress()
            e.printStackTrace()
            Utils.showToast(context, e.message)
        }
    }

    private fun removeDuplications() {
        var items = countriesList.toSet()
        countriesList.clear()
        for (item in items) {
            countriesList.add(item)
        }
    }

}
