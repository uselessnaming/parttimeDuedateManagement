package com.example.parttimeduedatemanagement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.part_timedatemanagement.ItemViewModel
import com.example.parttimeduedatemanagement.databinding.FragmentCountInventoryBinding

class CountInventoryFragment : Fragment() {

    private lateinit var binding : FragmentCountInventoryBinding
    private lateinit var mItemViewModel : ItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentCountInventoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun message(s : String){
        Toast.makeText(requireContext(),s,Toast.LENGTH_SHORT).show()
    }
    private fun initViewModel(){
        mItemViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(ItemViewModel::class.java)
    }
}