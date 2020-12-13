package com.esys.mviinitialproject.data.network.api

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
//    override suspend fun getUsers(): ResultWrapper<List<User>> {
//        val response = apiService.getUsersAsync()
//        return try {
//            if (response.isSuccessful) {
//                ResultWrapper.Success(response.body()!!)
//            } else {
//                ResultWrapper.Error(IOException("Error occurred during fetching users"))
//            }
//        } catch (e: Exception) {
//            ResultWrapper.Error(IOException("Unable to fetch users"))
//        }
//    }

}