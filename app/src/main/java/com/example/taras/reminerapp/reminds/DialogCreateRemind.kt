package com.example.taras.reminerapp.reminds

import android.app.Dialog
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
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.uiThread
import timber.log.Timber
import java.io.IOException

/**
 * Created by Taras Koloshmatin on 28.07.2018
 */
class DialogCreateRemind : DialogFragment() {

    private var mTypeServer: String = Constants.SERVER_DEFAULT

    companion object {
        const val TYPE_SERVER = "type_server"
        @JvmStatic
        fun newInstance(typeServer: String): DialogCreateRemind {
            val args = Bundle()
            args.putString(TYPE_SERVER, typeServer)
            val dialog = DialogCreateRemind()
            dialog.arguments = args
            return dialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle: Bundle? = arguments
        if (bundle != null) {
            mTypeServer = bundle.getString(TYPE_SERVER)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.fragment_create_remind)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        dialog.btn_cancel.onClick { dismiss() }
        dialog.btn_save.onClick {
            doAsync {
                val remind = Remind(dialog.title.text.toString(),
                        dialog.remind_date.text.toString(),
                        dialog.description.text.toString(),
                        Constants.TYPE_USER_REMIND)
                try {
                    ServiceGenerator.createService(RemindService::class.java).createRemind(remind).execute()
                    AppDatabase.getInstance().remindDao().insert(remind)
                } catch (e: IOException) {
                    Timber.e("Failed create user remind! ${e.message}")
                }
                uiThread {
                    dismiss()
                }
            }
        }
        return dialog
    }
}