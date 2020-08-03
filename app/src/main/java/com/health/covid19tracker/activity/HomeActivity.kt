package com.health.covid19tracker.activity

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.health.covid19tracker.R
import com.health.covid19tracker.adapter.MainAdapter
import com.health.covid19tracker.model.ResponseAll
import com.health.covid19tracker.network.Api
import com.health.covid19tracker.utils.Utils
import com.health.covid19tracker.utils.isOnline
import kotlinx.android.synthetic.main.activity_homectivity.*
import kotlinx.android.synthetic.main.activity_main.lastUpdatedTxv
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class HomeActivity : AppCompatActivity() {

    private lateinit var context: Context
    var isOnline = true
    lateinit var casesTxv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homectivity)
        casesTxv = findViewById(R.id.casesTxv)
        context = this@HomeActivity
        Utils.setLoadingWait(context)

        //Get the Stat from API
        getALlData()

        syncTxv.setOnClickListener {
            getALlData()
        }

        //Load the Main RCV with Following list data
        mainRcv.layoutManager = GridLayoutManager(context, 2)
        var list: ArrayList<String> = ArrayList()
        list.add("Country Wise")
        list.add("Clinical Management")
        list.add("Country  Map")
        mainRcv.adapter = MainAdapter(context, list)
    }

    override fun onResume() {
        super.onResume()
        getALlData()
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
                            //deathsTxv.text = stat?.deaths.toString()
                            //recoveredTxv.text = stat?.recovered.toString()
                            //lastUpdatedTxv
                            val calander1 = Calendar.getInstance(Locale.getDefault())
                            calander1.timeInMillis = stat!!.updated
                            val dateDisplay =
                                android.text.format.DateFormat.format("dd MMM hh:mm:aa", calander1)
                            lastUpdatedTxv.text =
                                resources.getString(R.string.last_updated_on) + dateDisplay
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
}
