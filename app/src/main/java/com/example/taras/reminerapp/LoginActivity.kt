package com.example.taras.reminerapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hanks.passcodeview.PasscodeView
import timber.log.Timber

/**
 * Created by Taras Koloshmatin on 28.07.2018
 */
class LoginActivity : AppCompatActivity(), PasscodeView.PasscodeViewListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var password = findViewById<PasscodeView>(R.id.password)
        password.listener = this
        password.localPasscode = "54321"
    }

    override fun onSuccess(number: String?) {
        if (number.equals("54321")) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        } else {
            Timber.d("Wrong password! $number")
        }
    }

    override fun onFail() {
        Timber.d("Wrong password!")
    }
}