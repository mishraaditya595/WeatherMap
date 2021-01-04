package com.vob.weathermap.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vob.weathermap.repository.Repository

class HomeViewModelFactory(private val context: Context, private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(context, repository) as T
    }
}