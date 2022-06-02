package com.mrq.paljobs.helpers

import android.app.Activity
import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Patterns
import android.view.Gravity
import android.widget.*
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

    fun isNotEmpty(textView: TextView, linear: LinearLayout): Boolean {
        return if (textView.text.toString().isBlank()) {
            linear.background = getDrawable(R.drawable.shape_red_stroke)
            textView.error = getString(R.string.empty_field)
            false
        } else {
            linear.background = getDrawable(R.drawable.shape_gray2)
            true
        }
    }

    fun isNotEmpty(editText: EditText): Boolean {
        return if (editText.text.toString().isBlank()) {
            editText.background = getDrawable(R.drawable.shape_red_stroke)
            editText.error = getString(R.string.empty_field)
            false
        } else {
            editText.background = getDrawable(R.drawable.shape_gray2)
            true
        }
    }

    fun isListNotEmpty(
        activity: Activity?,
        list: ArrayList<String>,
        linear: LinearLayout
    ): Boolean {
        return if (list.isEmpty()) {
            Constants.showAlert(activity!!, getString(R.string.must_select_skills), R.color.red)
            linear.setBackgroundResource(R.drawable.shape_red_stroke)
            false
        } else {
            linear.setBackgroundResource(R.drawable.shape_accent_stroke)
            true
        }
    }

    fun isStringNotEmpty(
        activity: Activity?,
        string: String,
    ): Boolean {
        return if (string.isEmpty()) {
            Constants.showAlert(activity!!, getString(R.string.must_select_photo), R.color.red)
            false
        } else {
            true
        }
    }

    fun isFileStringNotEmpty(
        activity: Activity?,
        string: String,
    ): Boolean {
        return if (string.isEmpty()) {
            Constants.showAlert(activity!!, getString(R.string.must_select_file), R.color.red)
            false
        } else {
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

    fun getText(editText: EditText): String {
        return editText.text.toString().trim()
    }


}