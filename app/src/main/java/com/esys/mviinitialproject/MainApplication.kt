package com.esys.mviinitialproject

import android.app.Application
import android.os.Environment
import com.balsikandar.crashreporter.CrashReporter
import com.esys.mviinitialproject.data.local.database.DbHelper
import com.esys.mviinitialproject.data.local.database.DbHelperImpl
import com.esys.mviinitialproject.data.local.preference.CommonPreference
import com.esys.mviinitialproject.data.local.preference.PreferenceHelper
import com.esys.mviinitialproject.data.local.preference.PreferenceHelperImpl
import com.esys.mviinitialproject.data.network.api.ApiHelper
import com.esys.mviinitialproject.data.network.api.ApiHelperImpl
import com.esys.mviinitialproject.data.network.api.RetrofitBuilder
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.io.File


class MainApplication : Application() {

    val apiHelper: ApiHelper by lazy {
        ApiHelperImpl(RetrofitBuilder.apiService)
    }

    val preferenceHelper: PreferenceHelper by lazy {
        PreferenceHelperImpl(CommonPreference(this))
    }

    val dbHelper: DbHelper by lazy {
        DbHelperImpl(this)
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

        val crashReporterPath = "/Android/data/$packageName/files/crashReports"
        val dir = File(Environment.getExternalStorageDirectory().toString() + crashReporterPath)
        if (!dir.exists() && !dir.isDirectory) {
            dir.mkdir()
        }
        CrashReporter.initialize(this, crashReporterPath)

    }

}