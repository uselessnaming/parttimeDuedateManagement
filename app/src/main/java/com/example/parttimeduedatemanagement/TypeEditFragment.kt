package com.example.parttimeduedatemanagement

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.part_timedatemanagement.Database.Item
import com.example.part_timedatemanagement.ItemViewModel
import com.example.parttimeduedatemanagement.Adapater.TypeEditAdapter
import com.example.parttimeduedatemanagement.databinding.FragmentTypeEditBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlinx.coroutines.launch

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
                adapter = mTypeEditAdapter
                val layout = LinearLayoutManager(context)
                layoutManager = layout
                setHasFixedSize(true)
            }
            mItemViewModel.viewModelScope.launch(Dispatchers.IO){
                try{
                    val locations = mItemViewModel.getType().await()
                    mTypeEditAdapter.submitList(locations)
                } catch(e : Exception){
                    throw IllegalStateException("viewModelScope in Error")
                }
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
}