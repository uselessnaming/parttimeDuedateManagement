package com.example.parttimeduedatemanagement

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.part_timedatemanagement.Database.Item
import com.example.parttimeduedatemanagement.ViewModel.ItemViewModel
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
        binding.apply{
            tvEditTitle.text = "카테고리 수정"
            rcTypeEdit.apply{
                mTypeEditAdapter.setOnClickListener(object : TypeEditAdapter.OnClickListener{
                    override fun onClick(typesHeader : String) {
                        val dialog = AlertDialog.Builder(requireContext())
                        dialog.apply{
                            setTitle("경고")
                            setMessage("정말 삭제하시겠습니까?")
                            setPositiveButton("확인") { dialog, btnType ->
                                when (btnType) {
                                    DialogInterface.BUTTON_POSITIVE -> {
                                        var messageText = ""
                                        mItemViewModel.viewModelScope.launch(Dispatchers.IO){
                                            val typeItems = mItemViewModel.checkType(typesHeader).await()
                                            messageText = if (typeItems.size == 1){
                                                mItemViewModel.deleteType(typesHeader,"")
                                                "삭제 완료"
                                            } else {
                                                "해당 타입에 속한 아이템이 있습니다.\n아이템 정보를 변경한 후 시도해주세요"
                                            }
                                            withContext(Dispatchers.Main){
                                                message(messageText)
                                            }
                                        }
                                        mItemViewModel.fetchTypes()
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
                mItemViewModel.types.observe(viewLifecycleOwner, Observer{
                    mTypeEditAdapter.submitList(it)
                })
                adapter = mTypeEditAdapter
                val layout = LinearLayoutManager(context)
                layoutManager = layout
                setHasFixedSize(true)
            }
            btnAddType.setOnClickListener{
                val dialog = TypeEditDialog()
                dialog.setBtnClickListener(object : TypeEditDialog.OnBtnClickListener{
                    override fun onClick(type : String) {
                        mItemViewModel.insert(Item(type,"",""))
                        Toast.makeText(context,"추가 완료",Toast.LENGTH_SHORT).show()
                        mItemViewModel.fetchTypes()
                        dialog.dismiss()
                    }
                })
                mActivity.createDialog(dialog,0,"typeEditDialog")
            }
        }
    }
    override fun onStart(){
        super.onStart()
        mItemViewModel.fetchTypes()
    }
    private fun initViewModel(){
        mItemViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(ItemViewModel::class.java)
    }
    private fun message(s : String){
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }
}