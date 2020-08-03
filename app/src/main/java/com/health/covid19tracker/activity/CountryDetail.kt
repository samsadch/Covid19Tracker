package com.health.covid19tracker.activity

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.health.covid19tracker.R
import com.health.covid19tracker.model.ResponseCountryDetail
import com.health.covid19tracker.network.Api
import com.health.covid19tracker.utils.Utils
import com.health.covid19tracker.utils.isOnline
import kotlinx.android.synthetic.main.activity_country.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountryDetail : AppCompatActivity() {
    lateinit var context: Context

    lateinit var country: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country)
        context = this@CountryDetail

        intent.extras?.getString("Country")?.let {
            country = it
            countryTitleTxv.text = it
        }

        getCountries()
    }

    var isOnline = true

    private fun getCountries() {
        try {
            if (!isOnline(context!!)) {
                isOnline = false
                showSnackBar(getString(R.string.no_internet))
            } else {
                isOnline = true
                Utils.setLoading(context)
                Utils.showProgress()
                val service = Api.createWithHeader(context)
                val call = service.getCountry(country)
                call.enqueue(object : Callback<ResponseCountryDetail> {
                    override fun onResponse(
                        call: Call<ResponseCountryDetail>,
                        response: Response<ResponseCountryDetail>
                    ) {
                        Utils.hideProgress()
                        try {
                            val stat = response.body()

                            stat?.let {
                                todayCasesTxv.text =
                                    todayCasesTxv.text.toString() + stat?.todayCases.toString()
                                todayDeathsTxv.text =
                                    todayDeathsTxv.text.toString() + stat?.todayDeaths.toString()
                                todayRecoveryTxv.text =
                                    todayRecoveryTxv.text.toString() + stat?.todayRecovered.toString()

                                totalCasesTxv.text =
                                    totalCasesTxv.text.toString() + stat?.cases.toString()
                                totalDeathsTxv.text =
                                    totalDeathsTxv.text.toString() + stat?.deaths.toString()
                                totalRecoveryTxv.text =
                                    totalRecoveryTxv.text.toString() + stat?.recovered.toString()
                            }

                            stat?.countryInfo?.let {
                                Glide.with(context)
                                    .load(stat?.countryInfo?.flag)
                                    .into(countryFlagImv)
                            }

                        } catch (e: Exception) {
                            Utils.hideProgress()
                            e.printStackTrace()
                            Utils.showToast(context, e.message)
                        }
                    }

                    override fun onFailure(call: Call<ResponseCountryDetail>, t: Throwable) {
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