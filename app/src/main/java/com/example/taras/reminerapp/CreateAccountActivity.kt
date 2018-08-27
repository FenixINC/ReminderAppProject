package com.example.taras.reminerapp

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.taras.reminerapp.databinding.ActivityCreateAccountBinding
import com.example.taras.reminerapp.db.model.Login
import com.example.taras.reminerapp.db.service.ServiceGenerator
import com.example.taras.reminerapp.db.service.UserService
import com.example.taras.reminerapp.utils.HtmlCompat
import okhttp3.ResponseBody
import org.jetbrains.anko.alert
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.sdk25.coroutines.onClick
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

/**
 * Created by Taras Koloshmatin on 26.08.2018
 */
class CreateAccountActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityCreateAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_account)

        mBinding.title = "Create account"

        mBinding.btnCancel.onClick {
            startActivity(intentFor<LoginActivity>())
            finish()
        }
        mBinding.btnCreate.onClick {
            mBinding.setShowProgress(true)

            val login = Login()
            login.username = mBinding.username.text.toString()
            login.password = mBinding.password.text.toString()

            val call: Call<ResponseBody> = ServiceGenerator.createService(UserService::class.java).create(login)
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    val message = response.body()?.string()?.replace("\"", "").toString()
                    if (response.isSuccessful) {
                        Timber.d(message)
                        mBinding.setShowProgress(false)
                        alert(HtmlCompat.fromHtml(message)) { positiveButton("Ok") {} }.show()
                        return
                    } else {
                        Timber.d("Login response successful.")
                        mBinding.setShowProgress(false)
                        startActivity(intentFor<LoginActivity>(
                                "message" to "User <b>${login.username}</b> was successfully created"))
                        finish()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                    Timber.w("Can not create. ${t?.message}")
                    mBinding.setShowProgress(false)
                    alert("Server unavailable!") { positiveButton("Ok") {} }.show()
                }
            })
        }
    }
}