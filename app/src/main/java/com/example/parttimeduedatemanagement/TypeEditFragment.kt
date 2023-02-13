package com.example.parttimeduedatemanagement

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.part_timedatemanagement.ItemViewModel
import com.example.parttimeduedatemanagement.Adapater.TypeEditAdapter
import com.example.parttimeduedatemanagement.databinding.FragmentTypeEditBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TypeEditFragment : Fragment() {

    private val TAG = "TypeEditFragment"
    private lateinit var binding : FragmentTypeEditBinding
    private lateinit var mItemViewModel : ItemViewModel
    private val mTypeEditAdapter by lazy{
        TypeEditAdapter()
    }
    private val mActivity by lazy{
        activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentTypeEditBinding.inflate(inflater,container,false)
        initViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TAG","onViewCreated fetchList()")
        fetchList()
        binding.apply{
            tvEditTitle.text = "카테고리 수정"
            rcTypeEdit.apply{
                mTypeEditAdapter.setOnClickListener(object : TypeEditAdapter.OnClickListener{
                    override fun onClick(typesHeader : String) {
                        val dialog = AlertDialog.Builder(requireContext())
                        dialog.apply{
                            var messageText = ""
                            setTitle("경고")
                            setMessage("정말 삭제하시겠습니까?")
                            setPositiveButton("확인") { dialog, btnType ->
                                when (btnType) {
                                    DialogInterface.BUTTON_POSITIVE -> {
                                        mItemViewModel.viewModelScope.launch(Dispatchers.IO){
                                            val typeItems = mItemViewModel.checkType(typesHeader).await()
                                            if (typeItems.size == 1){
                                                mItemViewModel.deleteType(typesHeader,"")
                                                messageText = "삭제 완료"
                                            } else {
                                                messageText = "해당 타입에 속한 아이템이 있습니다.\n아이템 정보를 변경한 후 시도해주세요"
                                            }
                                            withContext(Dispatchers.Main){
                                                message(messageText)
                                            }
                                        }
                                        dialog?.dismiss()
                                    }
                                    else -> dialog?.dismiss()
                                }
                            }
                            setNegativeButton("취소",null)
                            show()
                        }
                    }
                })
                adapter = mTypeEditAdapter
                val layout = LinearLayoutManager(context)
                layoutManager = layout
                setHasFixedSize(true)
            }
            btnAddType.setOnClickListener{
                val dialog = TypeEditDialog()
                mActivity.createDialog(dialog,0,"typeEditDialog")
            }
        }
    }
    private fun initViewModel(){
        mItemViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(ItemViewModel::class.java)
    }
    private fun message(s : String){
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }
    fun fetchList(){
        mItemViewModel.viewModelScope.launch(Dispatchers.IO){
            try{
                val locations = mItemViewModel.getType().await()
                mTypeEditAdapter.submitList(locations)
            } catch(e : Exception){
                throw IllegalStateException("viewModelScope in Error")
            }
        }
    }
}