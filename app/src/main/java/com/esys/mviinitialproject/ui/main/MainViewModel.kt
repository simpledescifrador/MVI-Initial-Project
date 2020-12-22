package com.esys.mviinitialproject.ui.main

import androidx.lifecycle.viewModelScope
import com.esys.mviinitialproject.data.model.User
import com.esys.mviinitialproject.data.network.api.ResultWrapper
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
    val mainIntent = Channel<MainIntent>(Channel.UNLIMITED)
    private val _mainState = MutableStateFlow<MainState>(MainState.Idle)
    val mainState: StateFlow<MainState>
        get() = _mainState

    sealed class MainIntent {
        object FetchUsers : MainIntent()
        data class GetUserDetails(val userId: Int) : MainIntent()
    }

    sealed class MainState {
        object Idle : MainState()
        object Loading : MainState()
        object NoNetworkAvailableError : MainState()
        data class LoadUsers(val users: List<User>) : MainState()
        data class LoadUserDetails(val user: User) : MainState()
        data class Error(val error: String?) : MainState()
    }

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            mainIntent.consumeAsFlow().collect {
                when (it) {
                    is MainIntent.FetchUsers -> fetchUsers()
                    is MainIntent.GetUserDetails -> loadUserDetails(it.userId)
                }
            }
        }
    }

    private fun loadUserDetails(userId: Int) {
        viewModelScope.launch {
            _mainState.value = MainState.Loading
            when (val result = repository.getUserDetail(userId)) {
                is ResultWrapper.Success -> {
                    _mainState.value = MainState.LoadUserDetails(result.data)
                }
                is ResultWrapper.Error -> {
                    _mainState.value = MainState.Error(result.exception?.message)
                }
                is ResultWrapper.NetworkError -> {
                    _mainState.value = MainState.NoNetworkAvailableError
                }
            }
        }
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            _mainState.value = MainState.Loading
            when (val result = repository.getUsers()) {
                is ResultWrapper.Success -> {
                    _mainState.value = MainState.LoadUsers(result.data)
                }
                is ResultWrapper.Error -> {
                    _mainState.value = MainState.Error(result.exception?.message)
                }
                is ResultWrapper.NetworkError -> {
                    _mainState.value = MainState.NoNetworkAvailableError
                }
            }
        }
    }
}