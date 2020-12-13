package com.esys.mviinitialproject.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job

abstract class BaseViewModel : ViewModel() {
    protected val viewModelScope = CoroutineScope(Dispatchers.IO + Job())
}