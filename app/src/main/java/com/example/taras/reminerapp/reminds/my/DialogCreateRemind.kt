package com.example.taras.reminerapp.reminds.my

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.Window
import android.view.WindowManager
import com.example.taras.reminerapp.R
import com.example.taras.reminerapp.db.AppDatabase
import com.example.taras.reminerapp.db.Constants
import com.example.taras.reminerapp.db.model.Remind
import kotlinx.android.synthetic.main.fragment_create_remind.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.uiThread

/**
 * Created by Taras Koloshmatin on 28.07.2018
 */
class DialogCreateRemind : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.fragment_create_remind)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        dialog.btn_cancel.onClick { dismiss() }
        dialog.btn_save.onClick {
            doAsync {
                AppDatabase.getInstance().remindDao().insert(Remind(
                        dialog.title.text.toString(),
                        dialog.remind_date.text.toString(),
                        dialog.description.text.toString(),
                        Constants.TYPE_USER_REMIND
                ))

                //TODO: also make POST request to server and save data there!

                uiThread {
                    dismiss()
                }
            }
        }
        return dialog
    }
}