package com.mufiid.assetmanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mufiid.assetmanagement.R
import com.mufiid.assetmanagement.models.Asset
import kotlinx.android.synthetic.main.item_data.view.*

class AssetAdapter(private val onClick: (Asset) -> Unit) :
    RecyclerView.Adapter<AssetAdapter.ViewHolder>() {

    val data = ArrayList<Asset>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(asset: Asset) {
            itemView.plat_mobil.text = asset.platMobil
            itemView.merk_mobil.text = asset.merkMobil
            itemView.setOnClickListener {
                onClick(asset)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_data, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount(): Int = data.size

    fun setAsset(asset: List<Asset>) {
        data.clear()
        data.addAll(asset)
        notifyDataSetChanged()
    }
}