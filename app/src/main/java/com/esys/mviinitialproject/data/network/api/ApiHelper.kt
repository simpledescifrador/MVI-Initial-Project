package com.esys.mviinitialproject.data.network.api

import com.esys.mviinitialproject.data.model.User

interface ApiHelper {
    suspend fun getUsers(): ResultWrapper<List<User>>
    suspend fun getUserDetail(userId: Int): ResultWrapper<User>
}