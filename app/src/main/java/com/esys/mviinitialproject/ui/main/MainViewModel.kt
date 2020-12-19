package com.esys.mviinitialproject.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.esys.mviinitialproject.data.local.database.DbHelper
import com.esys.mviinitialproject.data.local.preference.PreferenceHelper
import com.esys.mviinitialproject.data.network.api.ApiHelper
import com.esys.mviinitialproject.data.repository.MainRepository
import com.esys.mviinitialproject.ui.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainViewModel(private val repository: MainRepository) : BaseViewModel() {
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

    val mainIntent = Channel<MainIntent>(Channel.UNLIMITED)
    private val _mainState = MutableStateFlow<MainState>(MainState.Idle)
    val mainState: StateFlow<MainState>
        get() = _mainState

    sealed class MainIntent {
    }

    sealed class MainState {
        object Idle : MainState()
        object Loading : MainState()
        data class Error(val error: String?) : MainState()
    }

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            mainIntent.consumeAsFlow().collect {
            }
        }
    }
}