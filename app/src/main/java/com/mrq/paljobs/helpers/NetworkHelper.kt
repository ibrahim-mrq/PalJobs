package com.mrq.paljobs.helpers

import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.provider.Settings
import com.mrq.paljobs.R

object NetworkHelper {

    fun openWirelessSettings(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("No internet connection!")
            .setMessage("Do you want to open wireless internet settings?")
            .setPositiveButton(R.string.yes) { _: DialogInterface?, _: Int ->
                context.startActivity(
                    Intent(Settings.ACTION_WIRELESS_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
            .setNegativeButton(R.string.no) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
            .setIcon(R.drawable.ic_wireless)
            .show()
    }

    private fun getActiveNetworkInfo(context: Context): NetworkInfo? {
        return (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
    }

    private fun isConnected(context: Context): Boolean {
        val info = getActiveNetworkInfo(context)
        return info != null && info.isConnected
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun isNetworkOnline(context: Context): Boolean {
        var isOnline = false
        if (isConnected(context)) {
            try {
                val manager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val capabilities = manager.getNetworkCapabilities(manager.activeNetwork)
                isOnline = (capabilities != null
                        && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return isOnline
    }

}