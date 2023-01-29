package com.example.parttimeduedatemanagement

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
    private val deleteDialog by lazy{
        DeleteDialog(requireContext())
    }

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
                showDeleteDialog()
            }
            /* 수정 버튼 클릭 시 */
            btnUpdate.setOnClickListener{

            }
        }
    }

    private fun showDeleteDialog(){
        deleteDialog.apply{
            setOnOkClickedListener(object : DeleteDialog.OkClickedListener{
                override fun onOkClicked() {
                    val id = arguments?.getInt("mainId")
                    if(id == null){
                        throw NullPointerException("mainId is null value")
                    }
                    mItemViewModel.delete(mItemViewModel.searchItem(id))
                }
            })
            show()
        }
    }
}