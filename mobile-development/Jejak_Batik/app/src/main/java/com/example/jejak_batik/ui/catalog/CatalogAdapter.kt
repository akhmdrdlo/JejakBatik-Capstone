package com.example.jejak_batik.ui.catalog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jejak_batik.R
import com.example.jejak_batik.data.model.catalog.CatalogItem

class CatalogAdapter(
    private var catalogList: List<CatalogItem>,
    private val onItemClick: (CatalogItem) -> Unit
) : RecyclerView.Adapter<CatalogAdapter.CatalogViewHolder>() {

    inner class CatalogViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageViewBatik)
        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_batik_card, parent, false
        )
        return CatalogViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatalogViewHolder, position: Int) {
        val item = catalogList[position]
        holder.titleTextView.text = item.name
        holder.descriptionTextView.text = item.description
        Glide.with(holder.itemView.context)
            .load(item.link_image)
            .placeholder(R.drawable.ic_place_holder)
            .into(holder.imageView)
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount(): Int = catalogList.size

    fun updateList(newList: List<CatalogItem>) {
        catalogList = newList
        notifyDataSetChanged()
    }
}
