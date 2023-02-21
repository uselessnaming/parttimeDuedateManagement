package com.example.parttimeduedatemanagement

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Size
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.parttimeduedatemanagement.ViewModel.ItemViewModel
import com.example.parttimeduedatemanagement.databinding.FragmentTypeEditDialogBinding

class TypeEditDialog : DialogFragment() {

    private lateinit var binding : FragmentTypeEditDialogBinding
    private lateinit var mItemViewModel : ItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentTypeEditDialogBinding.inflate(layoutInflater, container, false)
        initViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply{
            btnDone.setOnClickListener{
                val type = etAddType.text.toString()
                listener.onClick(type)
            }
        }
    }
    override fun onResume(){
        super.onResume()
        dialogResize()
    }
    interface OnBtnClickListener{
        fun onClick(type : String) {
        }
    }
    private lateinit var listener : OnBtnClickListener
    fun setBtnClickListener(listener : OnBtnClickListener){
        this.listener = listener
    }
    private fun initViewModel(){
        mItemViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(ItemViewModel::class.java)
    }
    object ScreenMetricsCompat {
        private val api: Api =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) ApiLevel30()
            else Api()

        fun getScreenSize(context: Context): Size = api.getScreenSize(context)

        @Suppress("DEPRECATION")
        private open class Api {
            open fun getScreenSize(context: Context): Size {
                val display = context.getSystemService(WindowManager::class.java).defaultDisplay
                val metrics = if (display != null) {
                    DisplayMetrics().also { display.getRealMetrics(it) }
                } else {
                    Resources.getSystem().displayMetrics
                }
                return Size(metrics.widthPixels, metrics.heightPixels)
            }
        }

        @RequiresApi(Build.VERSION_CODES.R)
        private class ApiLevel30 : Api() {
            override fun getScreenSize(context: Context): Size {
                val metrics: WindowMetrics = context.getSystemService(WindowManager::class.java).currentWindowMetrics
                return Size(metrics.bounds.width(), metrics.bounds.height())
            }
        }
    }
    private fun dialogResize(){
        val size = ScreenMetricsCompat.getScreenSize(requireContext())
        val newWidth = (size.width * 0.7).toInt()
        val newHeight = (size.height * 0.3).toInt()
        val window = this.dialog?.window
        window?.setLayout(newWidth,newHeight)
    }
}