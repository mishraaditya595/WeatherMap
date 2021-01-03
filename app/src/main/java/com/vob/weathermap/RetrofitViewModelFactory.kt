package com.vob.weathermap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vob.weathermap.repository.Repository

class RetrofitViewModelFactory(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RetrofitViewModel(repository) as T
    }
}