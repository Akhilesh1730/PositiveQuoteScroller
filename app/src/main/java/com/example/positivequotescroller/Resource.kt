package com.example.positivequotescroller

import com.example.positivequotescroller.model.QuoteList
import com.example.positivequotescroller.model.QuoteListItem

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data = data)
    class Loading<T>() : Resource<T>()
    class Error<T>(message: String) : Resource<T>(message = message)
}