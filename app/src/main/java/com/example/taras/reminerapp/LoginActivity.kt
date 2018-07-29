package com.example.taras.reminerapp

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.taras.reminerapp.databinding.ActivityLoginBinding
import com.example.taras.reminerapp.db.AppDatabase
import com.example.taras.reminerapp.db.model.Remind
import com.example.taras.reminerapp.db.service.RemindService
import com.example.taras.reminerapp.db.service.ServiceGenerator
import com.hanks.passcodeview.PasscodeView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.uiThread
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

/**
 * Created by Taras Koloshmatin on 28.07.2018
 */
class LoginActivity : AppCompatActivity(), PasscodeView.PasscodeViewListener {

    lateinit var mBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        AppDatabase.getInstance()

        mBinding.password.listener = this
        mBinding.password.localPasscode = "54321"
    }

    override fun onSuccess(number: String?) {
        if (number.equals("54321")) {
            mBinding.setShowProgress(true)

            doAsync {
                try {
                    val response: Response<List<Remind>> = ServiceGenerator.createService(RemindService::class.java)
                            .getList().execute()

                    if (response.isSuccessful && response.body() != null) {
                        AppDatabase.getInstance().remindDao().delete()
                        AppDatabase.getInstance().remindDao().insert(response.body()!!)
                    } else {
                        Timber.e("Error loading reminds: ${response.code()}")
                    }
                } catch (e: IOException) {
                    Timber.e("Failed load reminds! ${e.message}")
                }
                uiThread {
                    mBinding.setShowProgress(false)
                    startActivity(intentFor<MainActivity>()) // or startActivity<MainActivity>()
//                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
            }
        } else {
            Timber.d("Wrong password! $number")
        }
    }

    override fun onFail() {
        Timber.d("Wrong password!")
    }
}