package com.example.taras.reminerapp

import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.Gravity
import com.example.taras.reminerapp.databinding.ActivityLoginBinding
import com.example.taras.reminerapp.db.AppDatabase
import com.example.taras.reminerapp.db.model.Login
import com.example.taras.reminerapp.db.service.ServiceGenerator
import com.example.taras.reminerapp.db.service.UserService
import com.example.taras.reminerapp.utils.HtmlCompat
import okhttp3.ResponseBody
import org.jetbrains.anko.*
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

        val message = intent?.extras?.getString("message")
        if (!TextUtils.isEmpty(message)) {
            MessageDialog(message!!)
        }

        //--- Login/server user login:
        mBinding.login.onClick {
            mBinding.setShowProgress(true)

            val username: String = mBinding.username.text.toString()
            val password: String = mBinding.password.text.toString()

            val login = Login()
            login.username = username
            login.password = password

            val call: Call<ResponseBody> = ServiceGenerator.createService(UserService::class.java).login(login)
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        val message = response.body()?.string()?.replace("\"", "").toString()
                        if (message.equals("Wrong password!", true)) {
                            Timber.d(message)
                            mBinding.setShowProgress(false)
                            toast(message)
                            return
                        } else {
                            Timber.d("Login response successful.")
                            mBinding.setShowProgress(false)
                            startActivity(intentFor<MainActivity>())
                            finish()
                        }
                    } else {
                        mBinding.setShowProgress(false)
                        alert("Wrong password, or user cannot be found!") {
                            positiveButton("Ok") {}
                            //TODO: rewrite customView like negativeButton:
                            customView {
                                linearLayout {
                                    padding = dip(16)
                                    button {
                                        text = "Create Account"
                                        isAllCaps = false
                                        textColor = Color.parseColor("#ffffff")
                                        background = ResourcesCompat.getDrawable(resources, R.drawable.background, null)
                                        gravity = Gravity.CENTER
                                        onClick {
                                            toast("Not yet implemented")
                                        }
                                    }.lparams(width = matchParent, height = wrapContent)
                                }
                            }
                        }.show()
                        return
                    }
                }

                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                    Timber.w("Can not create. ${t?.message}")
                    mBinding.setShowProgress(false)
                    alert("Server unavailable!") { positiveButton("Ok") {} }.show()
                }
            })
        }

        //--- Create new User:
        mBinding.createAccount.onClick {
            startActivity(intentFor<CreateAccountActivity>())
            finish()
        }

        //TODO: implement reset password:
        //--- Reset password:
//        mBinding.resetPassword.onClick {
//            startActivity(intentFor<CreateAccountActivity>("username" to mBinding.username.text.toString()))
//            finish()
//        }

        /*
        * pass parameters: startActivity(intentFor<CreateAccountActivity>("username" to mBinding.username.text.toString()))
        * getIntent: val intent = intent.extras
        *            val username: String? = intent?.getString("username")
        * */
    }

    private fun MessageDialog(message: String) {
        alert(HtmlCompat.fromHtml(message)) { positiveButton("Ok") {} }.show()
    }
}