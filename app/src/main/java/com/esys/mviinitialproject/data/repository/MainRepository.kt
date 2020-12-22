package com.esys.mviinitialproject.data.repository

import com.esys.mviinitialproject.data.local.database.DbHelper
import com.esys.mviinitialproject.data.local.preference.PreferenceHelper
import com.esys.mviinitialproject.data.model.User
import com.esys.mviinitialproject.data.network.api.ApiHelper
import com.esys.mviinitialproject.data.network.api.ResultWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class MainRepository(
    private val apiHelper: ApiHelper,
    private val preferenceHelper: PreferenceHelper,
    private val dbHelper: DbHelper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseRepository() {
    suspend fun getUsers(): ResultWrapper<List<User>> =
        safeApiCall(dispatcher, apiCall = { apiHelper.getUsers() })

    suspend fun getUserDetail(userId: Int): ResultWrapper<User> =
        safeApiCall(dispatcher, apiCall = { apiHelper.getUserDetail(userId) })
}