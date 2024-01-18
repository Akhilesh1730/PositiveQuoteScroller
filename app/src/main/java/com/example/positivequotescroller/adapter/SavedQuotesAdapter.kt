package com.example.positivequotescroller.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.positivequotescroller.databinding.SavedItemQuoteBinding
import com.example.positivequotescroller.model.SavedItem

class SavedQuotesAdapter(
    private val onItemClickListeners: OnItemClickListeners,
    private val dataSet : List<SavedItem>?) : RecyclerView.Adapter<SavedQuotesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SavedItemQuoteBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataSet!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setTextView(dataSet!!.get(position))
        holder.deleteImage.setOnClickListener {
            onItemClickListeners.onItemDeleted(position)
        }
    }

    interface OnItemClickListeners {
        fun onItemDeleted(position: Int)
    }

    class ViewHolder(binding: SavedItemQuoteBinding) : RecyclerView.ViewHolder(binding.root) {
        private val savedQuotesTextView = binding.textViewSavedQuotes
        private val savedAuthorTextView = binding.textViewSavedQuotesAuthor
        val deleteImage = binding.imageViewSavedQuotesDelete

        fun setTextView(item : SavedItem) {
            savedQuotesTextView.text = "\"${item.quotes}\""
            savedAuthorTextView.text = item.author
        }
    }
}