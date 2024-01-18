package com.example.positivequotescroller.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.positivequotescroller.Resource
import com.example.positivequotescroller.db.QuoteDBHelper
import com.example.positivequotescroller.model.QuoteList
import com.example.positivequotescroller.model.QuoteListItem
import com.example.positivequotescroller.model.SavedItem
import com.example.positivequotescroller.retro.QuoteService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Response
import java.util.concurrent.Flow

class QuotesRepository(private val quoteService: QuoteService, private val dbHelper: QuoteDBHelper) {
    private val quotesMutableFlow = MutableStateFlow<Resource<List<QuoteListItem>>>(Resource.Loading())
    val quotesFlow : StateFlow<Resource<List<QuoteListItem>>>
        get() = quotesMutableFlow

    private val _saveQuotesDbMutableFlow = MutableStateFlow<Resource<List<SavedItem>>>(Resource.Loading())
    val saveQuotesDbFlow : StateFlow<Resource<List<SavedItem>>>
        get() = _saveQuotesDbMutableFlow

    private val _addQuotesDbMutableFlow = MutableStateFlow<Resource<Long>>(Resource.Success(0L))
    val addQuotesDbFlow : StateFlow<Resource<Long>>
        get() = _addQuotesDbMutableFlow

    private val _deleteQuotesDbMutableFlow = MutableStateFlow<Resource<Long>>(Resource.Success(0L))
    val deleteQuotesDbFlow : StateFlow<Resource<Long>>
        get() = _deleteQuotesDbMutableFlow

    suspend fun getQuotes() {
        val response = quoteService.getQuotes()
        if (response.isSuccessful) {
            val quoteList = response.body()
            if (quoteList != null) {
                quotesMutableFlow.value = Resource.Success(quoteList)
            } else {
                quotesMutableFlow.value = Resource.Error("Something Went Wrong")
            }
        } else {
            quotesMutableFlow.value = Resource.Error("Something Went Wrong")
        }
    }

    fun addSavedData(item: QuoteListItem?) {
        if (dbHelper.checkQuotesAdded(item!!.q)) {
            _addQuotesDbMutableFlow.value = Resource.Error("Quote Already Added")
        } else {
            val status = dbHelper.addSavedQuotes(item)
            if (status != -1L) {
                _addQuotesDbMutableFlow.value = Resource.Success(1L)
            } else {
                _addQuotesDbMutableFlow.value = Resource.Error("Not able to Add")
            }
        }
    }

    fun getAllSavedQuotes() {
        _saveQuotesDbMutableFlow.value = Resource.Loading()
        val list = dbHelper.getSavedQuotes()
        if (list != null) {
            _saveQuotesDbMutableFlow.value = Resource.Success(list)
        } else {
            _saveQuotesDbMutableFlow.value = Resource.Error("Something Went Wrong")
        }
    }

    fun deleteQuotes(item: SavedItem) {
        val affectedRowCount = dbHelper.deleteSavedQuotes(item)
        if (affectedRowCount > 0) {
            _deleteQuotesDbMutableFlow.value = Resource.Success(1L)
        } else {
            _deleteQuotesDbMutableFlow.value = Resource.Error("Not Able to Delete")
        }
    }
}