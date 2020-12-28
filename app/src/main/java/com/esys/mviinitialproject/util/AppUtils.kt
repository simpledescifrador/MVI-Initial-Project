package com.esys.mviinitialproject.util

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Environment
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.CompositePermissionListener
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener
import com.karumi.dexter.listener.single.PermissionListener
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener
import timber.log.Timber
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class AppUtils {
    enum class LogType {
        INFO, DEBUG, WARNING, ERROR
    }

    companion object {

        private fun getTimeByFormat(format: String): String? {
            val c = Calendar.getInstance()
            val df = SimpleDateFormat(format)
            df.timeZone = TimeZone.getTimeZone(TimeZone.getDefault().id)
            return df.format(c.time)
        }

        @SuppressLint("SimpleDateFormat")
        fun convertTime(time: String): String {
            var convertedTime = time
            try {
                val sdf = SimpleDateFormat("H:mm")
                val dateObj = sdf.parse(time)
                convertedTime = SimpleDateFormat("hh:mm aa").format(dateObj)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return convertedTime
        }

        private fun getCurrentDateTime(format: String): String? {
            return getTimeByFormat(format)
        }

        fun getCurrentDateTime(): String? {
            return getTimeByFormat("dd-MMM-yyyy hh:mm:ss")
        }

        fun getCurrentTime(format: String): String? {
            return getTimeByFormat(format)
        }

        fun getCurrentTime(): String? {
            return getTimeByFormat("hh:mm")
        }

        fun getCurrentTimeInMillis(): Long {
            val currentTimeMillis: Long
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            currentTimeMillis = calendar.timeInMillis
            return currentTimeMillis
        }

        @Throws(ParseException::class)
        fun getDateFromString(date: String, format: String): Date? {
            @SuppressLint("SimpleDateFormat") val formatter = SimpleDateFormat(format)
            return formatter.parse(date)
        }

        fun hideKeyboard(activity: Activity) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun appendLog(context: Context, text: String?, logType: LogType) {
            val dialogPermissionListener: PermissionListener =
                DialogOnDeniedPermissionListener.Builder
                    .withContext(context)
                    .withTitle("Write Storage Permission")
                    .withMessage("Write Storage Permission is needed insert app logs")
                    .withButtonText(R.string.ok)
                    .build()


            Dexter.withContext(context)
                .withPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(CompositePermissionListener(dialogPermissionListener))
                .check()

            val pathDir = Environment.getExternalStorageDirectory()
            val applicationInfo = context.applicationInfo
            val stringId = applicationInfo.labelRes
            val applicationName =
                if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else context.getString(
                    stringId
                )
            val logFile = File(
                pathDir.path, "/${applicationName}.log"
            )
            if (!logFile.exists()) {
                try {
                    logFile.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                    Timber.e(e)
                }
            }
            try {
                //BufferedWriter for performance, true to set append to file flag
                val buf = BufferedWriter(FileWriter(logFile, true))

                buf.append("[${getCurrentDateTime("yyyy-MM-dd HH:mm:ss")}][$logType] $text")
                buf.newLine()
                buf.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }
}