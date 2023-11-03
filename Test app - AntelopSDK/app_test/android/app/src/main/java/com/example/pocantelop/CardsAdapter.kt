package com.example.pocantelop

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pocantelop.databinding.ItemCardBinding
import fr.antelop.sdk.digitalcard.DigitalCard

class CardsAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<CardsAdapter.CardsViewHolder>() {

    var cardList = ArrayList<DigitalCard>()
    var selectedCards = arrayListOf<DigitalCard>()

    fun setCards(cardList: List<DigitalCard>) {
        this.cardList = cardList as ArrayList<DigitalCard>
    }

    interface OnItemClickListener {
        fun onItemClick(card: DigitalCard, adapterPosition: Int)
        fun onItemLongClick(card: DigitalCard, adapterPosition: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardsViewHolder {
        var root = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return CardsViewHolder(root)
    }

    override fun onBindViewHolder(holder: CardsViewHolder, position: Int) {
        val binding = ItemCardBinding.bind(holder.itemView)
        val card = cardList[position]
        holder.bin(card, listener)
        if (selectedCards.contains(card))
            binding.viewMask.visibility = View.VISIBLE
        else
            binding.viewMask.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    class CardsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bin(cardModel: DigitalCard, listener: OnItemClickListener) {
            val binding = ItemCardBinding.bind(itemView)
            binding.textId.text = cardModel.bin
            binding.textHolderName.text = if (cardModel.expiryDate != null) cardModel.expiryDate.toString() else ""
            binding.textLastDigits.text = cardModel.lastDigits
            binding.root.setOnClickListener {
                listener.onItemClick(cardModel, adapterPosition)
            }

            binding.root.setOnLongClickListener {
                listener.onItemLongClick(cardModel, adapterPosition)
                true
            }
        }
    }
}