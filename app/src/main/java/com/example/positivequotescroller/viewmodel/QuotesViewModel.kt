package com.example.positivequotescroller.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.positivequotescroller.Resource
import com.example.positivequotescroller.model.QuoteList
import com.example.positivequotescroller.model.QuoteListItem
import com.example.positivequotescroller.model.SavedItem
import com.example.positivequotescroller.repo.QuotesRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QuotesViewModel(private val repository: QuotesRepository) : ViewModel() {

    fun getQuotes() {
        viewModelScope.launch {
            repository.getQuotes()
        }
    }

    fun addSavedQuote(item : QuoteListItem?) {
        viewModelScope.launch {
            repository.addSavedData(item)
        }
    }

    fun getSavedQuotes() {
        viewModelScope.launch {
            repository.getAllSavedQuotes()
        }
    }

    fun deleteSavedQuotes(item: SavedItem) {
        viewModelScope.launch {
            repository.deleteQuotes(item)
        }
    }
    fun getQuotesFlow(): StateFlow<Resource<List<QuoteListItem>>> {
        return repository.quotesFlow
    }

    fun getQuotesAddedFlow(): StateFlow<Resource<Long>> {
        return repository.addQuotesDbFlow
    }

    fun getSavedQuotesFlow(): StateFlow<Resource<List<SavedItem>>> {
        return repository.saveQuotesDbFlow
    }

    fun getSavedDeletedQuotesFlow(): StateFlow<Resource<Long>> {
        return repository.deleteQuotesDbFlow
    }
}