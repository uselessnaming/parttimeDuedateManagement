package com.example.parttimeduedatemanagement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.parttimeduedatemanagement.Adapater.CountInventoryAdapter
import com.example.parttimeduedatemanagement.Adapater.OnBtnClickListener
import com.example.parttimeduedatemanagement.ViewModel.ItemViewModel
import com.example.parttimeduedatemanagement.databinding.FragmentCountInventoryBinding

class CountInventoryFragment : Fragment() {

    private lateinit var binding : FragmentCountInventoryBinding
    private lateinit var mItemViewModel : ItemViewModel
    private val mCountInventoryAdapter by lazy{
        CountInventoryAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentCountInventoryBinding.inflate(layoutInflater, container, false)

        initViewModel()
        initRecyclerView()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        mItemViewModel.fetchEas()
    }

    private fun message(s : String){
        Toast.makeText(requireContext(),s,Toast.LENGTH_SHORT).show()
    }
    private fun initViewModel(){
        mItemViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(ItemViewModel::class.java)
    }
    private fun initRecyclerView(){
        binding.apply{
            rcvCountInventory.adapter = mCountInventoryAdapter
            mCountInventoryAdapter.setOnBtnClickListener(object : OnBtnClickListener {
                override fun onPlus(id : Int, ea : Int) {
                    mItemViewModel.updateEA(id, ea+1)
                    mItemViewModel.fetchEas()
                }
                override fun onMinus(id : Int, ea : Int) {
                    if (ea == 0){
                        message("현재 수량은 0개 입니다. 더 이상 줄일 수 없습니다.")
                        return
                    }
                    mItemViewModel.updateEA(id, ea-1)
                    mItemViewModel.fetchEas()
                }
            })
            val layoutManager = LinearLayoutManager(context)
            rcvCountInventory.layoutManager = layoutManager
            rcvCountInventory.setHasFixedSize(true)
        }
        mItemViewModel.eaData.observe(viewLifecycleOwner) {
            mCountInventoryAdapter.submitList(it)
        }
    }
}