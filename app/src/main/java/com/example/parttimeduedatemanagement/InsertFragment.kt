package com.example.parttimeduedatemanagement

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.lifecycle.*
import com.example.part_timedatemanagement.Base.BaseFragment
import com.example.part_timedatemanagement.Database.Item
import com.example.part_timedatemanagement.ItemViewModel
import com.example.parttimeduedatemanagement.databinding.FragmentInsertBinding
import java.util.stream.Collectors.toCollection

//menu에서 insert 클릭 시
class InsertFragment : BaseFragment() {
    private val TAG : String = "InsertFragment"
    private lateinit var binding : FragmentInsertBinding
    private lateinit var mItemViewModel : ItemViewModel
    private var items : List<String> = listOf()

    private val duedate by lazy{binding.etDuedateInsert.text.toString()}
    private val name by lazy{binding.etNameInsert.text.toString()}
    private val location by lazy{binding.etLocationInsert.text.toString()}

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

            /** 추가 사항을 입력한 후 버튼 클릭 시 */
            btnDone.setOnClickListener {
                /* 입력 창에 아무 것도 없으면 오류 출력 */
                if (etDuedateInsert.text.isEmpty() || etNameInsert.text.isEmpty()){
                    Log.d(TAG, "Error. input is nothing")
                }
                else{
                    val item = Item(location,name,duedate)
                    mItemViewModel.insert(item)
                    Log.d(TAG, "insert success")
                    /* 입력 창에 입력한 상품이 이미 있다면 이미 존재한다는 오류를 출력 */
                }
                /** 수정 사항 입력 후 버튼 클릭 시 */
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }
    private fun message(s : String){
        makeText(context, s, Toast.LENGTH_SHORT).show()
    }
}
