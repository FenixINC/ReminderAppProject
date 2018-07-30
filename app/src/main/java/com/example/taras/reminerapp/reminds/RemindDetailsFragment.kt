package com.example.taras.reminerapp.reminds

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.taras.reminerapp.databinding.ItemRemindDetailsBinding
import com.example.taras.reminerapp.db.model.Remind

/**
 * Created by Taras Koloshmatin on 30.07.2018
 */
class RemindDetailsFragment : Fragment() {

    private lateinit var mBinding: ItemRemindDetailsBinding
    private lateinit var mModel: Remind


    companion object {

        private const val KEY_MODEL = "key_model"

        @JvmStatic
        fun newInstance(model: Remind): RemindDetailsFragment {
            val args = Bundle()
            args.putParcelable(KEY_MODEL, model)
            val fragment = RemindDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = ItemRemindDetailsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        mModel = arguments!!.getParcelable(KEY_MODEL)
        val bundle = arguments
        if (bundle != null) {
            mModel = bundle.getParcelable(KEY_MODEL)
            mBinding.model = mModel
        }
    }
}