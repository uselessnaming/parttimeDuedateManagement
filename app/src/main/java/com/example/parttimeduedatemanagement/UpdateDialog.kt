package com.example.parttimeduedatemanagement

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
    private var itemId : Int = 0
    private var location = ""

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
            val item = mItemViewModel.searchItem(itemId).await()
            val duedate = item.date
            val year = duedate.substring(0,4)
            val month = duedate.substring(6,8)
            val day = duedate.substring(10,12)
            binding.apply{
                var index = 0
                if (item.location == "기타"){
                    index = 9
                } else {
                    index = item.location.toInt()-1
                }
                snChoiceLocation.setSelection(index)
                etEditName.setText(item.itemName)
                etUpdateYear.setText(year)
                etUpdateMonth.setText(month)
                etUpdateDay.setText(day)
            }
        }
        binding.apply{
            val locations = resources.getStringArray(R.array.locations)
            val adapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,locations)
            snChoiceLocation.adapter = adapter
            snChoiceLocation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                    location = locations[position]
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            btnCancel.setOnClickListener{
                dismiss()
            }
            btnDone.setOnClickListener{
                var date = binding.etUpdateYear.text.toString() + "년 "
                if (etUpdateMonth.text.length == 1){
                    date += "0"
                }
                date += etUpdateMonth.text.toString() + "월 "
                if (etUpdateDay.text.length == 1){
                    date += "0"
                }
                date += etUpdateDay.text.toString() + "일"
                mItemViewModel.update(itemId,etEditName.text.toString(),location,date)
                dismiss()
            }
        }
    }

    private fun initViewModel(){
        mItemViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(ItemViewModel::class.java)
    }
}