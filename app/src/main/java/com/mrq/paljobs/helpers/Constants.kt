@file:Suppress("CAST_NEVER_SUCCEEDS")

package com.mrq.paljobs.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.ColorRes
import com.mrq.paljobs.R
import com.mrq.paljobs.controller.activities.SplashActivity
import com.orhanobut.hawk.Hawk
import com.tapadoo.alerter.Alerter
import java.text.SimpleDateFormat
import java.util.*

object Constants {

    const val SERVER_KEY =
        "AAAAiqQIIM4:APA91bH01z5ium3Xe62U2xEj7sBKXmUiWEJ8qO5bcqg1wL1SqR0oHngBVH3g78RAt7LJR86_R-Jgek-F1yQ3eI3fl5bCW4lHdkz01VkzDNVxuZk0ParkY_Wncht9vFpSce6DTUrog6g_"

    const val TYPE_TITLE = "type_title"
    const val TYPE_ID = "type_id"
    const val TYPE_MODEL = "type_model"

    const val IS_LOGIN = "is_login"
    const val USER = "user"
    const val USER_TOKEN = "user_token"
    const val USER_TYPE = "user_type"
    const val TYPE_CUSTOMER = "customer"
    const val TYPE_COMPANY = "company"

    const val TYPE_LANGUAGE = "type_language"
    const val TYPE_LANGUAGE_AR = "ar"
    const val TYPE_LANGUAGE_EN = "en"

    @JvmStatic
    fun getDate(): String? {
        val sdf = SimpleDateFormat("dd/MMM/yyyy, hh:mm:ss", Locale.US)
        return sdf.format(Date())
    }

    @JvmStatic
    fun logout(context: Context) {
        Hawk.deleteAll()
        Toast.makeText(context, context.getString(R.string.logout), Toast.LENGTH_SHORT).show()
        context.startActivity(Intent(context, SplashActivity::class.java))
    }

    @JvmStatic
    fun showAlert(
        activity: Activity,
        text: String?,
        @ColorRes color: Int?,
    ) {
        Alerter.create(activity)
            .setTitle("")
            .setText(text!!)
            .setBackgroundColorRes(color!!)
            .setContentGravity(Gravity.CENTER)
            .enableSwipeToDismiss()
            .setDuration(2000)
            .hideIcon()
            .show()
    }

}