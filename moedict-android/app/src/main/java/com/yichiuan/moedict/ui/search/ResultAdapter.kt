package com.yichiuan.moedict.ui.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yichiuan.moedict.R
import com.yichiuan.moedict.ui.main.MainActivity
import java.util.*

class ResultAdapter internal constructor(context: Context, private val model: SearchViewModel
) : RecyclerView.Adapter<ResultAdapter.ResultViewHolder>(), View.OnClickListener {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    internal var results: ArrayList<Int>? = null
        set(results) {
            field = results
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val holder = ResultViewHolder(
                inflater.inflate(R.layout.item_search_result, parent, false))
        holder.itemView.setOnClickListener(this)
        return holder
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val word = model.getWord(this.results!![position])
        holder.item.text = word
        holder.itemView.tag = word
    }

    override fun getItemCount(): Int {
        return this.results!!.size
    }

    override fun onClick(view: View) {
        val context = inflater.context

        val show = Intent(context, MainActivity::class.java)
        show.putExtra(SearchManager.QUERY, view.tag as String)
        context.startActivity(show)
    }

    class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var item: TextView = itemView.findViewById(R.id.textview_item_result)
    }
}
