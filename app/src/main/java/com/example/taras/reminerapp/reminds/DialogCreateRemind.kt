package com.example.taras.reminerapp.reminds

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.Window
import android.view.WindowManager
import com.example.taras.reminerapp.R
import com.example.taras.reminerapp.db.AppDatabase
import com.example.taras.reminerapp.db.Constants
import com.example.taras.reminerapp.db.model.Remind
import com.example.taras.reminerapp.db.service.RemindService
import com.example.taras.reminerapp.db.service.ServiceGenerator
import kotlinx.android.synthetic.main.fragment_create_remind.*
import okhttp3.ResponseBody
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.okButton
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.alert
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

/**
 * Created by Taras Koloshmatin on 28.07.2018
 */
class DialogCreateRemind : DialogFragment() {

    companion object {
        @JvmStatic
        fun newInstance(): DialogCreateRemind {
            return DialogCreateRemind()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.fragment_create_remind)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        dialog.btn_cancel.onClick { dismiss() }
        dialog.btn_save.onClick {
            val remind = Remind(dialog.title.text.toString(),
                    dialog.remind_date.text.toString(),
                    dialog.description.text.toString(),
                    Constants.TYPE_USER_REMIND)

            val call: Call<ResponseBody> = ServiceGenerator.createService(RemindService::class.java).createRemind(remind)
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        Timber.d("Create remind response successful.")
                        doAsync {
                            AppDatabase.getInstance().remindDao().insert(remind)
                        }
                        var intent = Intent()
                        intent.putExtra("is_refresh", true)
                        targetFragment?.onActivityResult(targetRequestCode, 1, intent)
                        dialog.dismiss()
                        alert("Create remind successful.") {
                            okButton {}
                        }.show()
                    } else {
                        Timber.d("Cannot create remind: $remind")
                        alert("This remind already exists!") {
                            okButton {}
                        }.show()
                        return
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Timber.w("Failed create user remind! ${t.message}")
                    alert("Server unavailable!") { okButton {} }.show()
                }
            })
        }
        return dialog
    }
}