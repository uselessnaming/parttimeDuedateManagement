package com.example.parttimeduedatemanagement

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.part_timedatemanagement.Database.Item
import com.example.part_timedatemanagement.ItemViewModel
import com.example.parttimeduedatemanagement.databinding.FragmentBottomDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomDialog : BottomSheetDialogFragment(){

    private lateinit var mItemViewModel : ItemViewModel
    private val TAG = "BottomDialog"
    private lateinit var binding : FragmentBottomDialogBinding

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
        binding.apply{
            /* 삭제 버튼 클릭 시 */
            btnDelete.setOnClickListener{
                /* dialog 생성 */
                showAlertDialog()
            }
            /* 수정 버튼 클릭 시 */
            btnUpdate.setOnClickListener{

            }
        }
    }

    private fun showAlertDialog(){
        val dialog = AlertDialog.Builder(requireContext())
        dialog.apply{
            var listener = object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    when(p1){
                        DialogInterface.BUTTON_POSITIVE -> {
                            val id = arguments?.getInt("mainId")
                            if (id == null){
                                throw NullPointerException("id is null")
                            }
                            mItemViewModel.deleteItem(id)
                            dismiss()
                        }
                        else -> dismiss()
                    }
                }
            }
            setOnDismissListener(object : DialogInterface.OnDismissListener{
                override fun onDismiss(p0: DialogInterface?) {
                    mItemViewModel.fetchItems()
                    dismiss()
                }
            })
            setTitle("Warning")
            setMessage("Are you sure about that?")
            setPositiveButton("OK",listener)
            setNegativeButton("Cancel",null)
            show()
        }
    }
}