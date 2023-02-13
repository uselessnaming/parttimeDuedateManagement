package com.example.parttimeduedatemanagement

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
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
                val index = arguments?.getInt("position") ?: throw NullPointerException("index is NULL")
                snChoiceLocation.setSelection(index)
                etEditName.setText(item.itemName)
                etUpdateYear.setText(year)
                etUpdateMonth.setText(month)
                etUpdateDay.setText(day)
            }
        }
        binding.apply{
            var locations = listOf<String>()
            mItemViewModel.viewModelScope.launch(Dispatchers.IO){
                locations = mItemViewModel.getType().await()
                val adapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,locations)
                snChoiceLocation.adapter = adapter
                snChoiceLocation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        location = locations[position]
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
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
    override fun onResume(){
        super.onResume()
        dialogResize()
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
        val newHeight = (size.height * 0.4).toInt()
        val window = this.dialog?.window
        window?.setLayout(newWidth,newHeight)
    }
}