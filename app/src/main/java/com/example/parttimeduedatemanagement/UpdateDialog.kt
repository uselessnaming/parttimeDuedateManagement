package com.example.parttimeduedatemanagement

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Size
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.parttimeduedatemanagement.ViewModel.ItemViewModel
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

        binding.apply{
            mItemViewModel.fetchTypes()
            mItemViewModel.types.observe(viewLifecycleOwner, Observer{
                val types = it
                val adapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,types)
                snChoiceLocation.adapter = adapter
                snChoiceLocation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        location = types[position]
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            })
            btnCancel.setOnClickListener{
                dismiss()
            }
            btnDone.setOnClickListener{
                if (etUpdateYear.text.isBlank() || etUpdateMonth.text.isBlank() || etUpdateDay.text.isBlank()){
                    message("유통기한 란이 공백입니다. 빈칸 없이 입력해주세요")
                } else if (etEditName.text.isBlank()) {
                    message("이름 란이 공백입니다. 빈칸 없이 입력해주세요")
                } else if (etUpdateMonth.text.toString().toInt() !in 1..12) {
                    message("1년은 12월까지만 있습니다")
                } else {
                    val month = etUpdateMonth.text.toString()
                    val day = etUpdateDay.text.toString()
                    val name = etEditName.text.toString()
                    var date = etUpdateYear.text.toString() + "년 "

                    if (month.length == 1){
                        date += "0"
                    }
                    date += month + "월 "
                    if (day.length == 1){
                        date += "0"
                    }
                    date += day + "일"
                    onDoneClickListener.onClick(itemId,location, name, date)
                    mItemViewModel.updateIsEmpty(itemId,false)
                    dismiss()
                }
            }
            mItemViewModel.viewModelScope.launch(Dispatchers.IO){
                val item = mItemViewModel.searchItem(itemId).await()
                val duedate = item.date
                val year = if (duedate == "") {
                    ""
                } else {
                    duedate.substring(0,4)
                }
                val month = if (duedate == "") {
                    ""
                } else {
                    duedate.substring(6,8)
                }
                val day = if (duedate == "") {
                    ""
                } else {
                    duedate.substring(10,12)
                }
                etEditName.setText(item.itemName)
                etUpdateYear.setText(year)
                etUpdateMonth.setText(month)
                etUpdateDay.setText(day)
            }
        }
    }
    interface OnDoneClickListener{
        fun onClick(itemId : Int, type : String, name : String,date : String)
    }
    private lateinit var onDoneClickListener : OnDoneClickListener
    fun setOnDoneClickListener(listener : OnDoneClickListener){
        onDoneClickListener = listener
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
    private fun message(s : String){
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }
}