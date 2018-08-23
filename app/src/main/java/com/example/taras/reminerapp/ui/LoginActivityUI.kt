package com.example.taras.reminerapp.ui

import android.support.v4.content.ContextCompat
import android.view.Gravity
import com.example.taras.reminerapp.LoginActivity
import com.example.taras.reminerapp.R
import org.jetbrains.anko.*

/**
 * Created by Taras Koloshmatin on 23.08.2018
 */
class LoginActivityUI : AnkoComponent<LoginActivity> {
    override fun createView(ui: AnkoContext<LoginActivity>) = with(ui) {
        verticalLayout {
            gravity = Gravity.CENTER
            backgroundColor = ContextCompat.getColor(ctx, R.color.white)
            padding = dip(10)

            editText {
                id = R.id.username
                hint = "Username"
                hintTextColor = ContextCompat.getColor(ctx, R.color.text)
                textColor = ContextCompat.getColor(ctx, R.color.text)
            }.lparams(width = matchParent, height = wrapContent)

            editText {
                id = R.id.password
                hint = "Password"
                hintTextColor = ContextCompat.getColor(ctx, R.color.text)
                textColor = ContextCompat.getColor(ctx, R.color.text)
            }.lparams(width = matchParent, height = wrapContent) {
                topMargin = dip(10)
            }
        }
    }
}