package com.example.parttimeduedatemanagement

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.*
import com.example.part_timedatemanagement.Base.BaseFragment
import com.example.part_timedatemanagement.Database.Item
import com.example.part_timedatemanagement.ItemViewModel
import com.example.parttimeduedatemanagement.databinding.FragmentInsertBinding

//menu에서 insert 클릭 시
class InsertFragment : BaseFragment() {
    private val TAG : String = "InsertFragment"
    private lateinit var binding : FragmentInsertBinding
    private lateinit var mItemViewModel : ItemViewModel
    private val mActivity by lazy{
        activity as MainActivity
    }
    private lateinit var callback : OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInsertBinding.inflate(inflater, container, false)

        mItemViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
                .create(ItemViewModel::class.java)

        Log.d(TAG, "init Success")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.apply{
            var location = ""
            /** spinner 연결 */
            val locations = resources.getStringArray(R.array.locations)
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,locations)
            snLocationChoice.adapter = adapter
            snLocationChoice.setSelection(0)
            snLocationChoice.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    location = locations[position]
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

            /** 추가 사항을 입력한 후 버튼 클릭 시 */
            btnDone.setOnClickListener {
                /* 입력 창에 아무 것도 없으면 오류 출력 */
                if (etNameInsert.text.isEmpty()){
                    message("이름을 입력해주세요")
                } else if (etInputYear.text.isBlank() || etInputMonth.text.isBlank() || etInputDay.text.isBlank()){
                    message("날짜를 선택해주세요")
                } else{
                    val name = binding.etNameInsert.text.toString()
                    var date = binding.etInputYear.text.toString() + "년 "
                    if (etInputMonth.text.length == 1){
                        date += "0"
                    }
                    date += etInputMonth.text.toString() + "월 "
                    if (etInputDay.text.length == 1){
                        date += "0"
                    }
                    date += etInputDay.text.toString() + "일"

                    val item = Item(location,name,date)
                    mItemViewModel.insert(item)
                    message("추가 완료")

                    /* 추가 버튼을 눌렀을 시 fragment 전환 --> 초기 데이터를 전부 넣은 후에 이 기능을 넣는 것이 좋을 듯 mActivity.finishFragment() */
                    /* 입력 창에 입력한 상품이 이미 있다면 이미 존재한다는 오류를 출력 */
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }
    private fun message(s : String){
        makeText(context, s, Toast.LENGTH_SHORT).show()
    }
}
