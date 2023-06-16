package com.example.parttimeduedatemanagement

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Size
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.example.parttimeduedatemanagement.databinding.FragmentDuedateCheckBinding
import com.example.parttimeduedatemanagement.databinding.FragmentDuedateCheckDialogBinding

class DuedateCheckDialog : DialogFragment() {

    private lateinit var binding : FragmentDuedateCheckDialogBinding
    private lateinit var onButtonClickListener : OnButtonClickListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentDuedateCheckDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply{
            btnSoldOut.setOnClickListener{
                val dialog = AlertDialog.Builder(requireContext())
                dialog.apply{
                    setTitle("주의")
                    setMessage("날짜를 더 이상 갱신하지 않으시겠습니까?")
                    setPositiveButton("확인") { _, _ ->
                        onButtonClickListener.onSoldOutClick()
                    }
                    setNegativeButton("취소",null)
                }
                dialog.show()
            }
            btnUpdateDate.setOnClickListener{
                onButtonClickListener.onUpdateDateClick()
            }
        }
    }
    override fun onResume(){
        super.onResume()
        dialogResize()
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
        val newWidth = (size.width * 0.8).toInt()
        val newHeight = (size.height * 0.3).toInt()
        val window = this.dialog?.window
        window?.setLayout(newWidth,newHeight)
    }
    interface OnButtonClickListener{
        fun onSoldOutClick()
        fun onUpdateDateClick()
    }
    fun setOnButtonClickListener(onButtonClickListener : OnButtonClickListener){
        this.onButtonClickListener = onButtonClickListener
    }
}