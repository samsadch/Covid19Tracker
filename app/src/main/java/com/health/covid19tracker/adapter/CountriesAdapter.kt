package com.health.covid19tracker.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.health.covid19tracker.R
import com.health.covid19tracker.activity.CountryDetail
import com.health.covid19tracker.model.ResponseCountries


class CountriesAdapter(
    private val context: Context,
    private var list: ArrayList<ResponseCountries>
) : RecyclerView.Adapter<CountriesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val country: TextView = itemView.findViewById(R.id.nameTxv)
        val totalCasesTxv: TextView = itemView.findViewById(R.id.totalCasesTxv)
        val newCasesTxv: TextView = itemView.findViewById(R.id.newCasesTxv)
        val totalDeathsTxv: TextView = itemView.findViewById(R.id.totalDeathsTxv)
        val newDeathsTxv: TextView = itemView.findViewById(R.id.newDeathsTxv)
        val totalRecoveredTxv: TextView = itemView.findViewById(R.id.totalRecoveredTxv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Inflating the view
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item_counties, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        //assigning g values to the views
        holder.country.text = item.country
        holder.country.paintFlags = holder.country.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        holder.totalCasesTxv.text = item.cases.toString()
        holder.newCasesTxv.text = item.todayCases.toString()
        holder.totalDeathsTxv.text = item.deaths.toString()
        holder.newDeathsTxv.text = item.todayDeaths.toString()
        holder.totalRecoveredTxv.text = item.recovered.toString()

        if(item.todayCases>0){
            holder.newCasesTxv.setBackgroundResource(R.drawable.yellow_background)
        }

        //Checking the background
        if(item.todayDeaths>0){
            holder.newDeathsTxv.setBackgroundResource(R.drawable.red_background)
            holder.newDeathsTxv.setTextColor(ContextCompat.getColor(context,android.R.color.white))
        }

        //On clicking the country item showing the details
        holder.country.setOnClickListener {
            val intent = Intent(context!!,CountryDetail::class.java)
            intent.putExtra("Country",item.country)
            context.startActivity(intent)
        }

    }

}