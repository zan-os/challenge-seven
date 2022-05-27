package com.example.challenge_seven.utils

import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide

object Extension {

    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    fun String.isPasswordValid(): Boolean {
        return !TextUtils.isEmpty(this) && this.length >= 6
    }

    fun String.isUsernameValid(): Boolean {
        return !TextUtils.isEmpty(this) && this.length >= 3
    }

    fun Context.showLongToast(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    fun Context.loadImage(url: Any, into: ImageView) {
        Glide.with(this)
            .load(url)
            .into(into)
    }
}