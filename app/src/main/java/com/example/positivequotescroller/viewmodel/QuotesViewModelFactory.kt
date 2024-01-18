package com.example.positivequotescroller.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.positivequotescroller.repo.QuotesRepository

class QuotesViewModelFactory(private val repository: QuotesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return QuotesViewModel(repository) as T
    }
}