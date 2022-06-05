@file:Suppress("CAST_NEVER_SUCCEEDS")

package com.mrq.paljobs.helpers

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
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
import com.mrq.paljobs.models.Skills
import com.mrq.paljobs.models.Submit
import com.orhanobut.hawk.Hawk
import com.tapadoo.alerter.Alerter
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SetTextI18n,Range")
object Constants {

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

    const val TYPE_LANGUAGE = "type_language"
    const val TYPE_LANGUAGE_AR = "ar"
    const val TYPE_LANGUAGE_EN = "en"

    const val TYPE_FAVORITE = "favorite"
    const val TYPE_PROPOSAL = "proposal"

    const val TYPE_ADD = "add"
    const val TYPE_EDIT = "edit"

    const val TYPE_ABOUT = "about"
    const val TYPE_HELP = "help"
    const val TYPE_PRIVACY = "privacy"


    @JvmStatic
    fun getCurrentDate(): String? {
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm:ss", Locale.US)
        return sdf.format(Date())
    }

    @JvmStatic
    fun logout(context: Context) {
        Hawk.deleteAll()
        Hawk.put(IS_FIRST_START, true)
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
    fun ifItemInFavorite(
        product: Proposal,
        list: ArrayList<Favorite>,
        imageView: ImageView
    ): Boolean {
        val index = list.indexOfFirst { it.proposalId == product.id } // -1 if not found
        return if (index >= 0) {
            imageView.setImageResource(R.drawable.ic_save)
            true
        } else {
            imageView.setImageResource(R.drawable.ic_unsave)
            false
        }
    }

    @JvmStatic
    fun ifItemIsSubmit(
        context: Context,
        product: Favorite,
        list: ArrayList<Submit>,
        button: Button
    ): Boolean {
        val index = list.indexOfFirst { it.proposalId == product.proposalId } // -1 if not found
        return if (index >= 0) {
            button.setBackgroundResource(R.drawable.shape_gray)
            button.setText(R.string.submitted)
            button.setTextColor(ContextCompat.getColor(context, R.color.textPrimary))
            button.isEnabled = false
            true
        } else {
            button.setBackgroundResource(R.drawable.shape_accent)
            button.setText(R.string.submit_your_proposal)
            button.setTextColor(ContextCompat.getColor(context, R.color.white))
            button.isEnabled = true
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
            button.setText(R.string.submitted)
            button.setTextColor(ContextCompat.getColor(context, R.color.textPrimary))
            button.isEnabled = false
            true
        } else {
            button.setBackgroundResource(R.drawable.shape_accent)
            button.setText(R.string.submit_your_proposal)
            button.setTextColor(ContextCompat.getColor(context, R.color.white))
            button.isEnabled = true
            false
        }
    }

    @JvmStatic
    fun field(): ArrayList<String> {
        val list = ArrayList<String>()
        list.add("IT")
        list.add("Engineering")
        list.add("Trade")
        list.add("Science")
        return list
    }

    @JvmStatic
    fun fieldSkills(text: String): ArrayList<String> {
        val index = initFieldSkills().indexOfFirst { it.name == text } // -1 if not found
        return if (index >= 0) {
            initFieldSkills()[index].skills
        } else {
            arrayListOf()
        }
    }


    @JvmStatic
    private fun initFieldSkills(): ArrayList<Skills> {
        val list = ArrayList<Skills>()
        list.add(Skills("IT", itField()))
        list.add(Skills("Engineering", engineeringField()))
        list.add(Skills("Trade", tradeField()))
        list.add(Skills("Science", scienceField()))
        return list
    }

    private fun itField(): ArrayList<String> {
        val itSkills = ArrayList<String>()
        itSkills.add("Web")
        itSkills.add("Android")
        itSkills.add("IOS")
        itSkills.add("Graphic design")
        return itSkills
    }

    private fun engineeringField(): ArrayList<String> {
        val itSkills = ArrayList<String>()
        itSkills.add("Architectural Engineering")
        itSkills.add("Civil Engineering")
        itSkills.add("Computer Engineering")
        itSkills.add("Interior design")
        itSkills.add("Mechatronics")
        return itSkills
    }

    private fun tradeField(): ArrayList<String> {
        val itSkills = ArrayList<String>()
        itSkills.add("Accounting")
        itSkills.add("Electronic trade")
        itSkills.add("E-Marketing")
        return itSkills
    }

    private fun scienceField(): ArrayList<String> {
        val itSkills = ArrayList<String>()
        itSkills.add("Pharmacy")
        itSkills.add("Nursing")
        return itSkills
    }


}