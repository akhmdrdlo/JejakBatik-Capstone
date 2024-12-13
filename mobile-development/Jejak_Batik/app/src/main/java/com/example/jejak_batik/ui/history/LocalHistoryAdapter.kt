package com.example.jejak_batik.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jejak_batik.R
import com.example.jejak_batik.room.LocalHistoryEntity

class LocalHistoryAdapter : RecyclerView.Adapter<LocalHistoryAdapter.ViewHolder>() {

    private var historyList = listOf<LocalHistoryEntity>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val createdAtTextView: TextView = itemView.findViewById(R.id.TanggalTextView)
        val imageView: ImageView = itemView.findViewById(R.id.imageViewBatik)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = historyList[position]
        holder.titleTextView.text = item.title
        holder.createdAtTextView.text = item.createdAt

        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .placeholder(R.drawable.ic_place_holder)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = historyList.size

    fun updateData(newData: List<LocalHistoryEntity>) {
        historyList = newData
        notifyDataSetChanged()
    }
}
