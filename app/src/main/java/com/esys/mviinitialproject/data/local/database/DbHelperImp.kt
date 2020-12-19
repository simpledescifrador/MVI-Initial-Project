package com.esys.mviinitialproject.data.local.database

import android.content.Context

class DbHelperImp(private val context: Context) : DbHelper {
    private val appDatabase: AppDatabase by lazy {
        AppDatabase.getInstance(context)
    }
}