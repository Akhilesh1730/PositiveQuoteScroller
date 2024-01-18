package com.example.positivequotescroller.retro

import com.example.positivequotescroller.model.QuoteList
import com.example.positivequotescroller.model.QuoteListItem
import retrofit2.Response
import retrofit2.http.GET

interface QuoteService {

    @GET("quotes")
    suspend fun getQuotes() : Response<List<QuoteListItem>>
}