package com.esys.mviinitialproject.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.esys.mviinitialproject.data.local.database.DbHelper
import com.esys.mviinitialproject.data.local.preference.PreferenceHelper
import com.esys.mviinitialproject.data.network.api.ApiHelper
import com.esys.mviinitialproject.data.repository.MainRepository
import com.esys.mviinitialproject.ui.main.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ViewModelFactory(
    private val apiHelper: ApiHelper,
    private val preferenceHelper: PreferenceHelper,
    private val dbHelper: DbHelper
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(MainRepository(apiHelper, preferenceHelper, dbHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}