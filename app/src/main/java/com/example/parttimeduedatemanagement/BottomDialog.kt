package com.example.parttimeduedatemanagement

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.part_timedatemanagement.ItemViewModel
import com.example.parttimeduedatemanagement.databinding.FragmentBottomDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomDialog : BottomSheetDialogFragment(){

    private lateinit var mItemViewModel : ItemViewModel
    private val TAG = "BottomDialog"
    private lateinit var binding : FragmentBottomDialogBinding
    private val mActivity by lazy{
        activity as MainActivity
    }
    private var itemId : Int? = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomDialogBinding.inflate(layoutInflater, container,false)

        mItemViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(ItemViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemId = arguments?.getInt("mainId") ?: throw NullPointerException("id is null")

        binding.apply{
            /* 삭제 버튼 클릭 시 */
            btnDelete.setOnClickListener{
                /* dialog 생성 */
                showDeleteDialog()
            }
            /* 수정 버튼 클릭 시 */
            btnUpdate.setOnClickListener{
                /* dialog 생성 */
                val updateDialog = UpdateDialog()
                updateDialog.setOnDoneClickListener(object : UpdateDialog.OnDoneClickListener{
                    override fun onClick(itemId: Int, type : String, name : String, date : String) {
                        onDoneClickListener.onClick(itemId, name, type, date)
                    }
                })
                mActivity.createDialog(updateDialog,itemId!!,"updateDialog")
            }
        }
    }
    interface OnDoneClickListener{
        fun onClick(itemId : Int, name : String, type : String, date : String)
    }
    private lateinit var onDoneClickListener : OnDoneClickListener
    fun setOnDoneClickListener(listener : OnDoneClickListener){
        onDoneClickListener = listener
    }
    private fun showDeleteDialog(){
        val dialog = AlertDialog.Builder(requireContext())
        val listener = DialogInterface.OnClickListener { _, p1 ->
            when(p1){
                DialogInterface.BUTTON_POSITIVE -> {
                    mItemViewModel.deleteItem(itemId!!)
                    message("삭제되었습니다")
                    dismiss()
                }
                else -> dismiss()
            }
        }
        dialog.apply{
            setTitle("주의")
            setMessage("정말 삭제하시겠습니까?")
            setPositiveButton("확인",listener)
            setNegativeButton("취소",null)
            show()
        }
    }
    private fun message(s : String){
        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
    }
}