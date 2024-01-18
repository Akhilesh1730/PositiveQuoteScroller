package com.example.positivequotescroller.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.positivequotescroller.databinding.ItemQuoteBinding
import com.example.positivequotescroller.model.QuoteListItem

class QuotesAdapter constructor(private val dataSet : List<QuoteListItem>?, private val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<QuotesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemQuoteBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setTextView(dataSet!!.get(position))
        holder.saveImage.setOnClickListener {
            onItemClickListener.onItemSaved(position)
        }
    }

    override fun getItemCount(): Int {
        return dataSet!!.size
    }

    class ViewHolder(binding: ItemQuoteBinding) : RecyclerView.ViewHolder(binding.root) {
        private val quotesTextView = binding.textViewQuotes
        private val authorTextView = binding.textViewAuthor
        val saveImage = binding.imageViewQuotesBookmark

        fun setTextView(item : QuoteListItem) {
            quotesTextView.text = "${item.q}"
            authorTextView.text = "- ${item.a}"
        }

    }

    interface OnItemClickListener {
        fun onItemSaved(position: Int)
    }
}