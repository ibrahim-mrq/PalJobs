package com.mrq.paljobs.helpers

import android.app.Activity
import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Patterns
import android.view.Gravity
import android.widget.AutoCompleteTextView
import android.widget.CheckBox
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.mrq.paljobs.R
import com.tapadoo.alerter.Alerter

open class BaseActivity : AppCompatActivity() {

    private var dialog: Dialog? = null

    open fun showCustomProgress(setCancelable: Boolean) {
        if (dialog == null) {
            dialog = Dialog(
                this,
                android.R.style.Theme_Translucent_NoTitleBar_Fullscreen
            )
            dialog!!.setContentView(R.layout.include_loading_screen)
            dialog!!.setCancelable(setCancelable)
            dialog!!.show()
        } else {
            dialog!!.show()
        }
    }

    open fun dismissCustomProgress() {
        if (dialog != null) {
            dialog!!.dismiss()
            dialog = null
        }
    }

    open fun showAlert(
        activity: Activity?,
        text: String?,
        @ColorRes color: Int
    ) {
        Alerter.create(activity!!)
            .setTitle("")
            .setText(text!!)
            .setBackgroundColorRes(color)
            .setContentGravity(Gravity.CENTER)
            .enableSwipeToDismiss()
            .setDuration(2000)
            .hideIcon()
            .show()
    }

    open fun showAlertOffline(
        activity: Activity?,
        text: String?
    ) {
        Alerter.create(activity!!)
            .setTitle("")
            .setText(text!!)
            .setBackgroundColorRes(R.color.orange)
            .setContentGravity(Gravity.CENTER)
            .enableSwipeToDismiss()
            .setDuration(2000)
            .hideIcon()
            .show()
    }

    open fun showAlertError(
        activity: Activity?,
        text: String?
    ) {
        Alerter.create(activity!!)
            .setTitle("")
            .setText(text!!)
            .setBackgroundColorRes(R.color.red)
            .setContentGravity(Gravity.CENTER)
            .enableSwipeToDismiss()
            .setDuration(2000)
            .hideIcon()
            .show()
    }

    open fun showAlertSuccess(
        activity: Activity?,
        text: String?
    ) {
        Alerter.create(activity!!)
            .setTitle("")
            .setText(text!!)
            .setBackgroundColorRes(R.color.green_success)
            .setContentGravity(Gravity.CENTER)
            .enableSwipeToDismiss()
            .setDuration(2000)
            .hideIcon()
            .show()
    }

    fun isNotEmpty(editText: TextInputEditText, textInputLayout: TextInputLayout): Boolean {
        return if (editText.text.toString().isBlank()) {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error = getString(R.string.empty_field)
            false
        } else {
            textInputLayout.isErrorEnabled = false
            true
        }
    }

    fun isCheckBoxChecked(checkBox: CheckBox): Boolean {
        return if (!checkBox.isChecked) {
            checkBox.buttonTintList = ColorStateList.valueOf(Color.parseColor("#EE3636"))
            checkBox.error = getString(R.string.checkBox_field)
            false
        } else {
            checkBox.buttonTintList = ColorStateList.valueOf(Color.parseColor("#2699FB"))
            true
        }
    }

    fun isNotEmpty(editText: AutoCompleteTextView, textInputLayout: TextInputLayout): Boolean {
        return if (editText.text.toString().isBlank()) {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error = getString(R.string.empty_field)
            false
        } else {
            textInputLayout.isErrorEnabled = false
            true
        }
    }

    fun isValidEmail(editText: TextInputEditText, textInputLayout: TextInputLayout): Boolean {
        return if (Patterns.EMAIL_ADDRESS.matcher(editText.text.toString()).matches()) {
            textInputLayout.isErrorEnabled = false
            true
        } else {
            textInputLayout.error = getString(R.string.invalid_email)
            textInputLayout.isErrorEnabled = true
            false
        }
    }

    fun isPasswordLess(pass: TextInputEditText, text: TextInputLayout): Boolean {
        return if (pass.text.toString().trim().length >= 6) {
            text.isErrorEnabled = false
            true
        } else {
            text.error = getString(R.string.password_less_6_char)
            text.isErrorEnabled = true
            false
        }
    }

    fun isPasswordMatch(
        pass: TextInputEditText,
        text: TextInputLayout,
        re_pass: TextInputEditText,
        re_text: TextInputLayout
    ): Boolean {
        return if (pass.text.toString().trim() == re_pass.text.toString().trim()) {
            text.isErrorEnabled = false
            re_text.isErrorEnabled = false
            true
        } else {
            text.error = getString(R.string.password_not_match)
            text.isErrorEnabled = true
            re_text.error = getString(R.string.password_not_match)
            re_text.isErrorEnabled = true
            false
        }
    }

    fun getText(editText: TextInputEditText): String {
        return editText.text.toString().trim()
    }

    fun getText(editText: AutoCompleteTextView): String {
        return editText.text.toString().trim()
    }


}