package com.example.parttimeduedatemanagement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.parttimeduedatemanagement.Adapater.MemoAdapter
import com.example.parttimeduedatemanagement.ViewModel.ItemViewModel
import com.example.parttimeduedatemanagement.ViewModel.MemoViewModel
import com.example.parttimeduedatemanagement.databinding.FragmentMemoBinding

class MemoFragment : Fragment() {

    private val TAG = "MemoFragment"
    private lateinit var binding : FragmentMemoBinding
    private lateinit var mMemoViewModel : MemoViewModel
    private val mMemoAdapter by lazy{ MemoAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMemoBinding.inflate(layoutInflater, container, false)

        initViewModel()
        initRecyclerView()
        mMemoViewModel.fetchMemos()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        mMemoViewModel.fetchMemos()
    }

    private fun initViewModel(){
        mMemoViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(MemoViewModel::class.java)
    }
    private fun initRecyclerView(){
        binding.apply{
            rcvMemoTitle.adapter = mMemoAdapter
            val layoutManager = LinearLayoutManager(context)
            rcvMemoTitle.layoutManager = layoutManager
            rcvMemoTitle.setHasFixedSize(true)
        }
        mMemoViewModel.memos.observe(viewLifecycleOwner){
            mMemoAdapter.submitList(it)
        }
    }
}