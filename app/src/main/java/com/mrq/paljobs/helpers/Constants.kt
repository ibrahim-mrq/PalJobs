@file:Suppress("CAST_NEVER_SUCCEEDS")

package com.mrq.paljobs.helpers

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.mrq.paljobs.R
import com.mrq.paljobs.controller.activities.SplashActivity
import com.mrq.paljobs.models.Favorite
import com.mrq.paljobs.models.Proposal
import com.mrq.paljobs.models.Submit
import com.orhanobut.hawk.Hawk
import com.tapadoo.alerter.Alerter
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("SetTextI18n,Range")
object Constants {

    const val SERVER_KEY =
        "AAAAiqQIIM4:APA91bH01z5ium3Xe62U2xEj7sBKXmUiWEJ8qO5bcqg1wL1SqR0oHngBVH3g78RAt7LJR86_R-Jgek-F1yQ3eI3fl5bCW4lHdkz01VkzDNVxuZk0ParkY_Wncht9vFpSce6DTUrog6g_"

    const val REQUEST_COVER_GALLERY_CODE = 10001
    const val REQUEST_PHOTO_GALLERY_CODE = 10002
    const val REQUEST_FILE_CODE = 10003

    const val TYPE_TITLE = "type_title"
    const val TYPE_ID = "type_id"
    const val TYPE_MODEL = "type_model"

    const val IS_LOGIN = "is_login"
    const val IS_FIRST_START = "is_first_start"
    const val USER = "user"
    const val USER_TOKEN = "user_token"
    const val USER_TYPE = "user_type"
    const val TYPE_EMPLOYEE = "Employee"
    const val TYPE_COMPANY = "Company"

    const val TYPE_PHOTO = "photo"
    const val TYPE_PHOTO_COVER = "photoCover"

    const val TYPE_LANGUAGE = "type_language"
    const val TYPE_LANGUAGE_AR = "ar"
    const val TYPE_LANGUAGE_EN = "en"


    @JvmStatic
    fun getCurrentDate(): String? {
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm:ss", Locale.US)
        return sdf.format(Date())
    }

    @JvmStatic
    fun logout(context: Context) {
        Hawk.deleteAll()
        Toast.makeText(context, context.getString(R.string.logout_successfully), Toast.LENGTH_SHORT)
            .show()
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

    @JvmStatic
    fun getFileName(context: Context, uri: Uri): String? {
        val uriString = uri.toString()
        val myFile = File(uriString)
        var displayName: String? = null
        if (uriString.startsWith("content://")) {
            context.contentResolver.query(uri, null, null, null, null).use { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    displayName =
                        cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        } else if (uriString.startsWith("file://")) {
            displayName = myFile.name
        }
        Log.e("response", "displayName = $displayName")
        return displayName
    }

    @JvmStatic
    fun getDatePicker(context: Context, textView: TextView) {
        val format = "dd MMM yyyy"
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context, { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
                textView.text = dateFormat.format(calendar.time)
            }, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.show()
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
    }

    @JvmStatic
    fun getTimePicker(context: Context, textView: TextView) {
        val calendar = Calendar.getInstance()
        val picker = TimePickerDialog(
            context, { _: TimePicker?, hourOfDay: Int, minute: Int ->
                textView.text = getZeroPrefix(hourOfDay) + ":" + getZeroPrefix(minute)
            }, calendar[Calendar.HOUR_OF_DAY], calendar[Calendar.MINUTE], true
        )
        picker.show()
    }

    @JvmStatic
    fun getZeroPrefix(minute: Int): String {
        return if (minute < 10)
            "0$minute"
        else minute.toString()
    }

    @JvmStatic
    fun ifItemInFavorite(
        product: Proposal,
        list: ArrayList<Favorite>,
        imageView: ImageView
    ): Boolean {
        val index = list.indexOfFirst { it.proposalId == product.id } // -1 if not found
        return if (index >= 0) {
            imageView.setImageResource(R.drawable.ic_save)
//            imageView.isEnabled = false
            true
        } else {
            imageView.setImageResource(R.drawable.ic_unsave)
//            imageView.isEnabled = true
            false
        }
    }

    @JvmStatic
    fun ifItemIsSubmit(
        context: Context,
        product: Proposal,
        list: ArrayList<Submit>,
        button: Button
    ): Boolean {
        val index = list.indexOfFirst { it.proposalId == product.id } // -1 if not found
        return if (index >= 0) {
            button.setBackgroundResource(R.drawable.shape_gray)
            button.setText(R.string.submit_your_proposal)
            button.setTextColor(ContextCompat.getColor(context, R.color.textPrimary))
            button.isEnabled = true
            false
        } else {
            button.setBackgroundResource(R.drawable.shape_accent)
            button.setText(R.string.submitted)
            button.setTextColor(ContextCompat.getColor(context, R.color.white))
            button.isEnabled = false
            true
        }
    }

    @JvmStatic
    fun showFilterDialog(context: Activity) {
        val dialog = Dialog(context)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_filter)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setWindowAnimations(R.style.animationName)

        val params: ViewGroup.LayoutParams = context.window.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        context.window.attributes = params as WindowManager.LayoutParams


        val relativeClose = dialog.findViewById(R.id.relativeClose) as RelativeLayout
        relativeClose.setOnClickListener { dialog.dismiss() }


        dialog.show()

    }

}