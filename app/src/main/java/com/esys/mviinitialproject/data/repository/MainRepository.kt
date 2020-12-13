package com.esys.mviinitialproject.data.repository

import com.esys.mviinitialproject.data.network.api.ApiHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class MainRepository(
    private val apiHelper: ApiHelper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseRepository() {
    //SAMPLE
    //    suspend fun getUsers(): ResultWrapper<List<User>> =
    //        safeApiCall(dispatcher, apiCall = { apiHelper.getUsers() })
}