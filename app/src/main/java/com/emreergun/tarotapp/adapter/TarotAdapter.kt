package com.emreergun.tarotapp.adapter

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emreergun.tarotapp.R
import com.emreergun.tarotapp.model.TarotModel


class TarotViewHolder(view:View):RecyclerView.ViewHolder(view){
    val context=view.context

    val cardImage:ImageView=view.findViewById(R.id.cardImage)

}
class TarotAdapter(val tarotList: ArrayList<TarotModel>):RecyclerView.Adapter<TarotViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarotViewHolder {

        val root=LayoutInflater.from(parent.context).inflate(R.layout.card_item,parent,false)

        return TarotViewHolder(root)


    }

    override fun getItemCount(): Int {
        return tarotList.size
    }

    override fun onBindViewHolder(holder: TarotViewHolder, position: Int) {

        val tarotCard=tarotList[position]
        Glide
            .with(holder.context)
            .load(tarotCard.imageUrl)
            .apply(RequestOptions().placeholder(R.drawable.tarotcover))
            .into(holder.cardImage)

    }
}