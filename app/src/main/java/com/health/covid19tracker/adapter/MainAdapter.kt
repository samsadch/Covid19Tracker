package com.health.covid19tracker.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.health.covid19tracker.R
import com.health.covid19tracker.activity.MainActivity
import com.health.covid19tracker.activity.MapActivity
import com.health.covid19tracker.activity.PdfViewActivity

//Main Menu Adapter
class MainAdapter(var context: Context, private var list: ArrayList<String>) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Inflate the view
        val view = inflater.inflate(R.layout.list_item_cards, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.itemTxv.text = item

        //On Click of items load the Next activity accordingly
        holder.mainRlay.setOnClickListener {
            when (item) {
                "Country Wise" -> {
                    context!!.startActivity(Intent(context, MainActivity::class.java))
                }
                "Clinical Management" -> {
                    context!!.startActivity(Intent(context, PdfViewActivity::class.java))
                }
                "Country  Map" -> {
                    context!!.startActivity(Intent(context, MapActivity::class.java))
                }
                else -> {

                }
            }
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTxv: TextView = itemView.findViewById(R.id.itemTxv)
        val mainRlay: RelativeLayout = itemView.findViewById(R.id.mainRlay)
    }
}