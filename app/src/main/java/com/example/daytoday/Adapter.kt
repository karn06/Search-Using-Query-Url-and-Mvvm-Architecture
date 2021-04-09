package com.example.daytoday

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.daytoday.model.Pages
import kotlinx.android.synthetic.main.search_item.view.*
import java.lang.StringBuilder
import java.util.*

class Adapter : RecyclerView.Adapter<Adapter.RecyclerViewHolder>() {

    var pages = ArrayList<Pages>()
    lateinit var context: Context

    lateinit var clickListener: onClickListener

    fun setData(context: Context, pages: ArrayList<Pages>, onClickListener: onClickListener) {
        this.pages = pages
        this.context = context
        this.clickListener = onClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.search_item, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {

        var pagePosition = pages[position]
        holder.apply {

            if (pagePosition.thumbnail != null)
                Glide.with(context)
                    .load(pagePosition.thumbnail.source).into(profilePicture)

            title.text = pagePosition.title
            var stringBuilder = StringBuilder()
            if (pagePosition.terms != null)
                if (pagePosition.terms.description.isNotEmpty()) {
                    for (i in pagePosition.terms.description.indices) {
                        stringBuilder.append(pagePosition.terms.description[i])
                    }
                }
            description.text = stringBuilder

            item.setOnClickListener {
                clickListener.onClick(pagePosition.fullurl)
            }
        }
    }

    override fun getItemCount(): Int {
        return pages.size;
    }

    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profilePicture = view.profilePicture
        val title = view.title
        val description = view.description
        val item = view.layout

    }

    interface onClickListener {
        fun onClick(url: String)
    }

}