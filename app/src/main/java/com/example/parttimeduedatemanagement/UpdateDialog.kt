package com.example.parttimeduedatemanagement

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.part_timedatemanagement.ItemViewModel
import com.example.parttimeduedatemanagement.databinding.FragmentUpdateDialogBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateDialog : DialogFragment(){

    private val TAG = "UpdateDialog"

    private lateinit var binding : FragmentUpdateDialogBinding
    private lateinit var mItemViewModel : ItemViewModel
    private var itemId : Int? = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateDialogBinding.inflate(layoutInflater, container, false)
        initViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemId = arguments?.getInt("itemId") ?: throw NullPointerException("itemId is Null")
        mItemViewModel.viewModelScope.launch(Dispatchers.IO){
            val item = mItemViewModel.searchItem(itemId!!).await()
            binding.etEditDuedate.setText(item.date)
            binding.etEditLocation.setText(item.location)
            binding.etEditName.setText(item.itemName)
        }
        // 문제 발생 데이터가 return되지 않음

        binding.apply{
            btnCancel.setOnClickListener{
                Log.d(TAG,"Cancel clicked")
                dismiss()
            }
            btnDone.setOnClickListener{
                Log.d(TAG,"done clicked")
                mItemViewModel.update(itemId!!,etEditDuedate.text.toString(),etEditLocation.text.toString(),etEditName.text.toString())
                dismiss()
            }
        }
    }

    private fun initViewModel(){
        mItemViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(ItemViewModel::class.java)
    }
}