package com.example.parttimeduedatemanagement

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.parttimeduedatemanagement.Adapater.GoneItemAdapter
import com.example.parttimeduedatemanagement.Adapater.ItemAdapter
import com.example.parttimeduedatemanagement.Adapater.ItemViewHolder
import com.example.parttimeduedatemanagement.Adapater.OnItemClickListener
import com.example.parttimeduedatemanagement.Database.Item
import com.example.parttimeduedatemanagement.LiveData.observe
import com.example.parttimeduedatemanagement.ViewModel.ItemViewModel
import com.example.parttimeduedatemanagement.databinding.FragmentSoldOutCheckBinding

class SoldOutCheckFragment : Fragment() {

    private lateinit var binding : FragmentSoldOutCheckBinding

    private lateinit var mItemViewModel : ItemViewModel

    private val soldOutAdapter by lazy{
        GoneItemAdapter()
    }

    private val mActivity by lazy{
        activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSoldOutCheckBinding.inflate(layoutInflater, container, false)

        initVM()
        initRcv()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mItemViewModel.fetchSoldOutList()
    }

    private fun initVM(){
        mItemViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(ItemViewModel::class.java)
    }

    private fun initRcv(){
        binding.apply{
            rcvItems.adapter = soldOutAdapter

            soldOutAdapter.setGoneItemClickListener(object : OnItemClickListener {
                override fun onClick(item: Item) {
                    val dialog = UpdateDialog()
                    dialog.setOnDoneClickListener(object : UpdateDialog.OnDoneClickListener{
                        override fun onClick(itemId: Int,type: String,name: String,date: String,) {
                            mItemViewModel.update(itemId, name, type, date)
                            mItemViewModel.fetchSoldOutList()
                            dialog.dismiss()
                        }
                    })
                    mActivity.createDialog(dialog,item.id,"updateDialog")
                }
            })
            val layout = LinearLayoutManager(context)
            rcvItems.layoutManager = layout
            rcvItems.setHasFixedSize(true)
        }
        mItemViewModel.soldOutList.observe(viewLifecycleOwner){
            soldOutAdapter.submitList(it)
        }
    }
}