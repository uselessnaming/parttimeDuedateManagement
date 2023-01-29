package com.example.parttimeduedatemanagement

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import com.example.parttimeduedatemanagement.databinding.DeleteDialogBinding

class DeleteDialog (
    context : Context
) : Dialog(context){

    private val TAG = "DeleteDialog"
    private lateinit var binding : DeleteDialogBinding
    private lateinit var okListener : OkClickedListener

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = DeleteDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() = with(binding){
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.apply{
            /* 취소 버튼을 눌렀을 때 */
            btnCancel.setOnClickListener{
                Log.d(TAG,"Cancel is clicked")
                dismiss()
            }
            /* Ok 버튼을 눌렀을 때 */
            btnOk.setOnClickListener{
                Log.d(TAG, "Ok is clicked")
                okListener.onOkClicked()
            }
        }
    }

    fun setOnOkClickedListener(listener : OkClickedListener){
        okListener = listener
    }

    interface OkClickedListener{
        fun onOkClicked()
    }
}