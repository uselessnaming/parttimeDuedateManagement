package com.example.parttimeduedatemanagement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.part_timedatemanagement.ItemViewModel
import com.example.parttimeduedatemanagement.Adapater.TypeEditAdapter
import com.example.parttimeduedatemanagement.databinding.FragmentTypeEditBinding
import kotlinx.coroutines.NonDisposableHandle.parent

class TypeEditFragment : Fragment() {

    private val TAG = "TypeEditFragment"
    private lateinit var binding : FragmentTypeEditBinding
    private lateinit var mItemViewModel : ItemViewModel
    private val mTypeEditAdapter by lazy{
        TypeEditAdapter()
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
        mTypeEditAdapter.submitList(mItemViewModel.types)
    }
    private fun initViewModel(){
        mItemViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(ItemViewModel::class.java)
    }
}