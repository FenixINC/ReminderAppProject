package com.example.taras.reminerapp.reminds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.taras.reminerapp.BaseFragment
import com.example.taras.reminerapp.databinding.ItemRemindDetailsBinding
import com.example.taras.reminerapp.db.model.Remind

/**
 * Created by Taras Koloshmatin on 30.07.2018
 */
class RemindDetailsFragment : BaseFragment() {

    private lateinit var mBinding: ItemRemindDetailsBinding
    private var mModel: Remind? = Remind()


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            val bundle: Bundle? = arguments
            if (bundle != null) {
                mModel = bundle.getParcelable(KEY_MODEL)
            }
        }
    }

    override fun onCreateFragmentView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = ItemRemindDetailsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.model = mModel
        mToolbar.title = mModel?.title
    }
}