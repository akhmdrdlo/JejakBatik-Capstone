package com.example.jejak_batik.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jejak_batik.R
import com.example.jejak_batik.data.model.history.HistoryItem

class HistoryAdapter(
    private var historyList: List<HistoryItem>,
    private val onItemClick: (HistoryItem) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageViewBatik)
        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        val tanggalTextView: TextView = view.findViewById(R.id.TanggalTextView)

        init {
            view.setOnClickListener {
                onItemClick(historyList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = historyList[position]

        holder.titleTextView.text = item.result
        holder.tanggalTextView.text = "Tanggal: ${item.createdAt}"

        Glide.with(holder.itemView.context)
            .load(item.imageUrl ?: R.drawable.ic_place_holder)
            .placeholder(R.drawable.ic_place_holder)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = historyList.size

    fun updateData(newData: List<HistoryItem>) {
        historyList = newData
        notifyDataSetChanged()
    }
}
