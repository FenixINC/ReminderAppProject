package com.example.taras.reminerapp

import android.content.DialogInterface
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.text.TextUtils
import android.view.Gravity
import android.widget.EditText
import android.widget.LinearLayout
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

    private lateinit var mBinding: ActivityLoginBinding
    private lateinit var mDialog: DialogInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        AppDatabase.getInstance()

//        val message = intent?.extras?.getString("message")
//        if (!TextUtils.isEmpty(message)) {
//            messageDialog(message!!)
//        }

        //--- Login/server user login:
        mBinding.login.onClick {
            if (!TextUtils.isEmpty(mBinding.username.text.toString())
                    && !TextUtils.isEmpty(mBinding.password.text.toString())) {

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
                                okButton {}
                                neutralPressed("Create Account") {
                                    customDialog(false)
                                }
                            }.show()
                            return
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                        Timber.w("Can not create. ${t?.message}")
                        mBinding.setShowProgress(false)
                        alert("Server unavailable!") { okButton {} }.show()
                    }
                })
            } else if (!TextUtils.isEmpty(mBinding.username.text.toString())
                    && TextUtils.isEmpty(mBinding.password.text.toString())) {
                messageDialog("Please, enter the password!")
            } else if (TextUtils.isEmpty(mBinding.username.text.toString())
                    && !TextUtils.isEmpty(mBinding.password.text.toString())) {
                messageDialog("Please, enter the username!")
            } else {
                messageDialog("Username and password cannot be empty!")
            }
        }


        //--- Create new User:
        mBinding.createAccount.onClick {
            customDialog(false)
        }

        //--- Reset password:
        mBinding.resetPassword.onClick {
            customDialog(true)
        }

        /*
        * pass parameters: startActivity(intentFor<CreateAccountActivity>("username" to mBinding.username.text.toString()))
        * getIntent: val intent = intent.extras
        *            val username: String? = intent?.getString("username")
        * */
    }


    private fun messageDialog(message: String) {
        alert(HtmlCompat.fromHtml(message)) { okButton {} }.show()
    }

    private fun customDialog(isResetPassword: Boolean) {
        val messageTitle: String
        val messageButton: String
        var usernameEditText = EditText(ctx)
        var passwordEditText = EditText(ctx)

        if (isResetPassword) {
            messageTitle = "Reset Password"
            messageButton = "Reset"
        } else {
            messageTitle = "Create Account"
            messageButton = "Create"
        }

        mDialog = alert {
            customView {
                linearLayout {
                    orientation = LinearLayout.VERTICAL
                    padding = dip(20)

                    textView {
                        text = messageTitle
                        textSize = sp(8).toFloat()
                        textColor = ResourcesCompat.getColor(resources, R.color.text, null)
                        gravity = Gravity.CENTER
                    }

                    linearLayout {
                        orientation = LinearLayout.VERTICAL

                        usernameEditText = editText {
                            hint = "Username"
                            hintTextColor = ResourcesCompat.getColor(resources, R.color.text, null)
                            textColor = ResourcesCompat.getColor(resources, R.color.text, null)
                            background = ResourcesCompat.getDrawable(resources, R.drawable.background_white, null)
                            inputType = InputType.TYPE_CLASS_TEXT
                        }.lparams(
                                width = matchParent,
                                height = wrapContent
                        )

                        view {
                            background = ResourcesCompat.getDrawable(resources, R.color.grey, null)
                        }.lparams(
                                width = matchParent,
                                height = dip(1)
                        )

                        passwordEditText = editText {
                            hint = "Password"
                            hintTextColor = ResourcesCompat.getColor(resources, R.color.text, null)
                            textColor = ResourcesCompat.getColor(resources, R.color.text, null)
                            background = ResourcesCompat.getDrawable(resources, R.drawable.background_white, null)
                            inputType = InputType.TYPE_CLASS_TEXT
                        }.lparams(
                                width = matchParent,
                                height = wrapContent
                        )
                    }.lparams(
                            width = matchParent,
                            height = wrapContent) {
                        topMargin = dip(10)
                    }

                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL

                        button {
                            text = messageButton
                            isAllCaps = false
                            padding = dip(10)
                            textColor = ResourcesCompat.getColor(resources, R.color.white, null)
                            background = ResourcesCompat.getDrawable(resources, R.drawable.background, null)
                            gravity = Gravity.CENTER
                            onClick {
                                val username = usernameEditText.text.toString()
                                val password = passwordEditText.text.toString()
                                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                                    mDialog.dismiss()
                                    createAccount(username, password)
                                } else if (TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                                    messageDialog("Please, enter username!")
                                } else if (!TextUtils.isEmpty(username) && TextUtils.isEmpty(password)) {
                                    messageDialog("Please, enter password!")
                                } else {
                                    messageDialog("Username and password cannot be empty!")
                                }
                            }
                        }.lparams(
                                width = wrapContent,
                                height = wrapContent,
                                weight = 1f) {
                            marginEnd = dip(5)
                        }

                        button {
                            text = "Cancel"
                            isAllCaps = false
                            padding = dip(10)
                            textColor = ResourcesCompat.getColor(resources, R.color.white, null)
                            background = ResourcesCompat.getDrawable(resources, R.drawable.background, null)
                            gravity = Gravity.CENTER
                            onClick {
                                mDialog.dismiss()
                            }
                        }.lparams(
                                width = wrapContent,
                                height = wrapContent,
                                weight = 1f) {
                            marginStart = dip(5)
                        }
                    }.lparams(
                            width = matchParent,
                            height = wrapContent) {
                        topMargin = dip(30)
                    }
                }
            }
        }.show()
    }

    private fun createAccount(username: String, password: String) {
        val login = Login()
        login.username = username
        login.password = password
        val call: Call<ResponseBody> = ServiceGenerator.createService(UserService::class.java).create(login)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val message: String = response.body()?.string()?.replace("\"", "").toString()
                if (response.isSuccessful) {
                    Timber.d("Login response successful.")
                    mBinding.setShowProgress(false)
                    alert(HtmlCompat.fromHtml("User <b>${login.username}</b> was successfully created")) {
                        okButton {}
                    }.show()

                } else {
                    Timber.d("Cannot create user. This user is already exists!")
                    mBinding.setShowProgress(false)
                    alert(HtmlCompat.fromHtml(
                            "Cannot create user. This user <b>${login.username}</b> is already exists!")) {
                        okButton {}
                    }.show()
                    return
                }
            }

            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                Timber.w("Can not create. ${t?.message}")
                mBinding.setShowProgress(false)
                alert("Server unavailable!") { okButton {} }.show()
            }
        })
    }
}