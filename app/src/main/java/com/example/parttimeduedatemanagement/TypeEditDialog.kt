package com.example.parttimeduedatemanagement

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.part_timedatemanagement.Database.Item
import com.example.part_timedatemanagement.ItemViewModel
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
                val location = etAddType.text.toString()
                mItemViewModel.insert(Item(location,"",""))
                mItemViewModel.fetchItems()
                dismiss()
                Toast.makeText(context,"추가 완료",Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onResume(){
        super.onResume()

    }
    private fun initViewModel(){
        mItemViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(ItemViewModel::class.java)
    }
}