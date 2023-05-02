package com.example.parttimeduedatemanagement

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.parttimeduedatemanagement.Database.Item
import com.example.parttimeduedatemanagement.Adapater.CountInventoryAdapter
import com.example.parttimeduedatemanagement.Adapater.OnBtnClickListener
import com.example.parttimeduedatemanagement.Adapater.OnEditorActionListener
import com.example.parttimeduedatemanagement.Memo.MemoFragment
import com.example.parttimeduedatemanagement.ViewModel.ItemViewModel
import com.example.parttimeduedatemanagement.databinding.FragmentCountInventoryBinding

class CountInventoryFragment : Fragment() {

    private lateinit var binding : FragmentCountInventoryBinding
    private lateinit var mItemViewModel : ItemViewModel
    private var sb = StringBuilder()

    private val mCountInventoryAdapter by lazy{
        CountInventoryAdapter()
    }
    private val mActivity by lazy{
        activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentCountInventoryBinding.inflate(layoutInflater, container, false)
        sb.clear()
        initItemViewModel()
        initRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply{
            btnDone.setOnClickListener{
                mItemViewModel.fetchItems("stringBuilder")
                val memoFragment = MemoFragment()
                val bundle = Bundle()
                bundle.putString("content",sb.toString())
                memoFragment.arguments = bundle
                message("확인")
                mActivity.switchFragment(memoFragment)
            }
            btnCancel.setOnClickListener{
                mActivity.onBackPressed()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mItemViewModel.fetchItems("")
    }

    private fun message(s : String){
        Toast.makeText(requireContext(),s,Toast.LENGTH_SHORT).show()
    }
    private fun initItemViewModel(){
        mItemViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(ItemViewModel::class.java)
    }
    private fun initRecyclerView(){
        binding.apply{
            rcvCountInventory.adapter = mCountInventoryAdapter
            mCountInventoryAdapter.setOnBtnClickListener(object : OnBtnClickListener {
                override fun onPlus(id : Int, ea : Int) {
                    mItemViewModel.updateEA(id, ea+1)
                    mItemViewModel.fetchItems("stringBuilder")
                }
                override fun onMinus(id : Int, ea : Int) {
                    if (ea == 0){
                        message("현재 수량은 0개 입니다. 더 이상 줄일 수 없습니다.")
                        return
                    }
                    mItemViewModel.updateEA(id, ea-1)
                    mItemViewModel.fetchItems("stringBuilder")
                }
            })
            mCountInventoryAdapter.setOnEditorActionListener(object : OnEditorActionListener {
                override fun onEnterClick(item: Item, ea : Int) {
                    mItemViewModel.updateEA(item.id, ea)
                    mItemViewModel.fetchItems("stringBuilder")
                }
            })
            val layoutManager = LinearLayoutManager(context)
            rcvCountInventory.layoutManager = layoutManager
            rcvCountInventory.setHasFixedSize(true)
        }
        mItemViewModel.items.observe(viewLifecycleOwner) {
            mCountInventoryAdapter.submitList(it)
            this.sb = mItemViewModel.getStringBuilder()
        }
    }
}