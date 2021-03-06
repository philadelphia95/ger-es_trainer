package com.example.geres_trainer.screens.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.geres_trainer.database.translation.TranslationDBDao
import java.lang.IllegalArgumentException

class ViewFragmentViewModelFactory (
    private val dataSource: TranslationDBDao
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ViewFragmentViewModel::class.java)){
            return ViewFragmentViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}