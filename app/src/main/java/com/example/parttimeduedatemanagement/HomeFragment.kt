package com.example.parttimeduedatemanagement

import android.os.Bundle
import android.view.*
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.part_timedatemanagement.Base.BaseFragment
import com.example.part_timedatemanagement.ItemViewModel
import com.example.parttimeduedatemanagement.Adapater.ItemContainerAdapter
import com.example.parttimeduedatemanagement.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment() {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var mItemViewModel : ItemViewModel
    private lateinit var mItemContainerAdapter : ItemContainerAdapter
    //private lateinit var mItems : LiveData<List<Item>>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)

        initRecyclerView(binding.itemList)
        initViewModel()

        val recyclerView : RecyclerView = binding.itemList
        recyclerView.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    /** ViewModel 초기화 */
    private fun initViewModel(){
        mItemViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(ItemViewModel::class.java)
        mItemViewModel.getLocation().observe(viewLifecycleOwner, Observer{
            mItemContainerAdapter.setLocations(it)
        })
    }

    /** itemContainerRecyclerview를 초기화 */
    private fun initRecyclerView (rcv : RecyclerView){
        mItemContainerAdapter = ItemContainerAdapter()
        rcv.run{
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = mItemContainerAdapter
        }
    }

    private fun setList(position : Int){

    }


}
