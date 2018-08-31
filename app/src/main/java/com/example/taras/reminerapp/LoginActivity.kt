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
import com.example.taras.reminerapp.db.Constants
import com.example.taras.reminerapp.db.model.Login
import com.example.taras.reminerapp.db.model.Remind
import com.example.taras.reminerapp.db.service.RemindService
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
import java.io.IOException
import java.lang.ref.WeakReference

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

        //--- Login:
        mBinding.login.onClick {
            if (!TextUtils.isEmpty(mBinding.username.text.toString())
                    && !TextUtils.isEmpty(mBinding.password.text.toString())) {

                mBinding.setShowProgress(true)
                login(mBinding.username.text.toString(), mBinding.password.text.toString())
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
            mBinding.setShowProgress(false)
            customDialog(false)
        }

        //--- Reset password:
        mBinding.resetPassword.onClick {
            mBinding.setShowProgress(false)
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
        val hintPassword: String

        var usernameEditText = EditText(ctx)
        var passwordEditText = EditText(ctx)

        if (isResetPassword) {
            messageTitle = "Reset Password"
            messageButton = "Reset"
            hintPassword = "Enter new password"
        } else {
            messageTitle = "Create Account"
            messageButton = "Create"
            hintPassword = "Password"
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
                            textSize = 16f
                            hint = "Username"
                            hintTextColor = ResourcesCompat.getColor(resources, R.color.text, null)
                            typeface = ResourcesCompat.getFont(applicationContext, R.font.font_sansation_light)
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
                            textSize = 16f
                            hint = hintPassword
                            hintTextColor = ResourcesCompat.getColor(resources, R.color.text, null)
                            typeface = ResourcesCompat.getFont(applicationContext, R.font.font_sansation_light)
                            textColor = ResourcesCompat.getColor(resources, R.color.text, null)
                            background = ResourcesCompat.getDrawable(resources, R.drawable.background_white, null)
                            inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
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
                            textSize = 18f
                            textColor = ResourcesCompat.getColor(resources, R.color.white, null)
                            typeface = ResourcesCompat.getFont(applicationContext, R.font.font_sansation_bold)
                            background = ResourcesCompat.getDrawable(resources, R.drawable.background, null)
                            gravity = Gravity.CENTER
                            onClick {
                                val username = usernameEditText.text.toString()
                                val password = passwordEditText.text.toString()
                                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                                    mDialog.dismiss()
                                    if (!isResetPassword) {
                                        mBinding.setShowProgress(true)
                                        createAccount(username, password)
                                    } else {
                                        mBinding.setShowProgress(true)
                                        resetPassword(username, password)
                                    }
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
                            textSize = 18f
                            textColor = ResourcesCompat.getColor(resources, R.color.white, null)
                            typeface = ResourcesCompat.getFont(applicationContext, R.font.font_sansation_bold)
                            background = ResourcesCompat.getDrawable(resources, R.drawable.background, null)
                            gravity = Gravity.CENTER
                            onClick {
                                mDialog.dismiss()
                                mBinding.setShowProgress(false)
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
                        topMargin = dip(40)
                    }
                }
            }
        }.show()
    }

    private fun login(username: String, password: String) {
        val login = Login()
        login.username = username
        login.password = password

        val call: Call<ResponseBody> = ServiceGenerator.createService(UserService::class.java).userLogin(login)
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

                        doAsync {
                            val weakReference: WeakReference<LoginActivity> = WeakReference(this@LoginActivity)
                            var list: List<Remind>? = null

                            if (weakReference.get() != null) {
                                try {
                                    val responseRemind: Response<List<Remind>> = ServiceGenerator.createService(RemindService::class.java)
                                            .getListByType(Constants.TYPE_USER_REMIND).execute()

                                    if (responseRemind.isSuccessful) {
                                        list = responseRemind.body()!!
                                        AppDatabase.getInstance().remindDao().delete()
                                        AppDatabase.getInstance().remindDao().insert(list)
                                    } else {
                                        Timber.e("Error loading reminds: ${responseRemind.code()}")
                                    }
                                } catch (e: IOException) {
                                    Timber.e("Failed load reminds! ${e.message}")
                                }
                            } else {
                                Timber.d("LoginActivity weakReference is null!")
                            }
                            uiThread {
                                mBinding.setShowProgress(false)
                                startActivity(intentFor<MainActivity>())
                                finish()
                            }
                        }
                    }
                } else {
                    Timber.d("Wrong password or user cannot be found!")
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
                Timber.w("Cannot login. ${t?.message}")
                mBinding.setShowProgress(false)
                alert("Server unavailable!") { okButton {} }.show()
            }
        })
    }

    private fun createAccount(username: String, password: String) {
        val login = Login()
        login.username = username
        login.password = password

        val call: Call<ResponseBody> = ServiceGenerator.createService(UserService::class.java).userCreate(login)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Timber.d("Login response successful.")
                    mBinding.setShowProgress(false)
                    alert(HtmlCompat.fromHtml("User <b>${login.username}</b> was successfully created.")) {
                        okButton {}
                    }.show()
                } else {
                    Timber.d("Cannot create user. This user ${login.username} is already exists!")
                    mBinding.setShowProgress(false)
                    alert(HtmlCompat.fromHtml(
                            "Cannot create user. This user <b>${login.username}</b> is already exists!")) {
                        okButton {}
                    }.show()
                    return
                }
            }

            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                Timber.w("Cannot create user. ${t?.message}")
                mBinding.setShowProgress(false)
                alert("Server unavailable!") { okButton {} }.show()
            }
        })
    }

    private fun resetPassword(username: String, password: String) {
        val login = Login()
        login.username = username
        login.password = password
        val call: Call<ResponseBody> = ServiceGenerator.createService(UserService::class.java).userResetPassword(login)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Timber.d("Reset password response successful.")
                    mBinding.setShowProgress(false)
                    alert(HtmlCompat.fromHtml("Successful reset password for user <b>${login.username}</b>.")) {
                        okButton {}
                    }.show()
                } else {
                    Timber.d("Cannot reset password. This user <b>${login.username}</b> is absent!")
                    mBinding.setShowProgress(false)
                    alert(HtmlCompat.fromHtml(
                            "Cannot reset password!")) {
                        okButton {}
                    }.show()
                    return
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Timber.w("Can not reset password. ${t.message}")
                mBinding.setShowProgress(false)
                alert("Server unavailable!") { okButton {} }.show()
            }
        })
    }
}