package com.esys.mviinitialproject

import android.app.Application
import android.os.Environment
import com.balsikandar.crashreporter.CrashReporter
import java.io.File

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val crashReporterPath = "/Android/data/$packageName/files/crashReports"
        val dir = File(Environment.getExternalStorageDirectory().toString() + crashReporterPath)
        if (!dir.exists() && !dir.isDirectory) {
            dir.mkdir()
        }
        CrashReporter.initialize(this, crashReporterPath)

    }


}