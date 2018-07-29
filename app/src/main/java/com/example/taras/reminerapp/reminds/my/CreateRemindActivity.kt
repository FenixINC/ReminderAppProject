package com.example.taras.reminerapp.reminds.my

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.taras.reminerapp.R
import com.example.taras.reminerapp.databinding.ActivityCreateRemindBinding
import com.example.taras.reminerapp.db.AppDatabase
import com.example.taras.reminerapp.db.Constants
import com.example.taras.reminerapp.db.model.Remind
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by Taras Koloshmatin on 28.07.2018
 */
class CreateRemindActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityCreateRemindBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_remind)

        mBinding.btnSave.setOnClickListener {
            doAsync {
                AppDatabase.getInstance().remindDao().insert(Remind(
                        mBinding.title.text.toString(),
                        mBinding.remindDate.text.toString(),
                        mBinding.description.text.toString(),
                        Constants.TYPE_USER_REMIND
                ))

                //TODO: also make POST request to server and save data there!

                uiThread {
                    finish()
                }
            }
        }
    }
}