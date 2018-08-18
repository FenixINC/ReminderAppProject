package com.example.taras.reminerapp

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.taras.reminerapp.databinding.ActivityLoginBinding
import com.example.taras.reminerapp.db.AppDatabase
import com.example.taras.reminerapp.db.model.Login
import com.example.taras.reminerapp.db.service.ServiceGenerator
import com.example.taras.reminerapp.db.service.UserService
import okhttp3.ResponseBody
import org.jetbrains.anko.alert
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.sdk25.coroutines.onClick
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

/**
 * Created by Taras Koloshmatin on 28.07.2018
 */
class LoginActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        AppDatabase.getInstance()

        mBinding.login.onClick {
            mBinding.setShowProgress(true)

            val username: String = mBinding.username.text.toString()
            val password: String = mBinding.password.text.toString()

            val login: Login = Login()
            login.setUsername(username)
            login.setPassword(password)

            val call: Call<ResponseBody> = ServiceGenerator.createService(UserService::class.java).login(login)
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                    if (response!!.isSuccessful) {
                        val message = response.body()?.source().toString()
                        if (message.equals("true", true)) {

                        }
                        Timber.d("Login response successful.")
                        mBinding.setShowProgress(false)
                        startActivity(intentFor<MainActivity>())
                        finish()
                    } else {
                        mBinding.setShowProgress(false)
                        return
                    }
                }

                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                    Timber.w("Can not login.")
                    mBinding.setShowProgress(false)
                    alert {
                        message = "ERROR RESPONSE!!"
                    }
                }
            })

//            doAsync {
//                try {
//                    val call: Call<ResponseBody> = ServiceGenerator.createService(UserService::class.java).login(login)
//                    val response: Response<ResponseBody> = call.execute()
//                    if (response.isSuccessful) {
//
//                    }
//
////                    if (response.isSuccessful && response.body() != null) {
////                        AppDatabase.getInstance().remindDao().delete()
////                        AppDatabase.getInstance().remindDao().insert(response.body()!!)
////                    } else {
////                        Timber.e("Error loading reminds: ${response.code()}")
////                    }
//                } catch (e: IOException) {
//                    Timber.e("Failed load reminds! ${e.message}")
//                }
//                uiThread {
//                    mBinding.setShowProgress(false)
//                    startActivity(intentFor<MainActivity>()) // or startActivity<MainActivity>()
////                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
//                    finish()
//                }
//            }
        }
    }
}