package com.example.parttimeduedatemanagement

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.part_timedatemanagement.Database.Item
import com.example.parttimeduedatemanagement.ViewModel.ItemViewModel
import com.example.parttimeduedatemanagement.Adapater.GoneItemAdapter
import com.example.parttimeduedatemanagement.databinding.FragmentDuedateCheckBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DuedateCheckFragment : Fragment() {

    private val TAG = "DuedateCheckFragment"
    private lateinit var binding : FragmentDuedateCheckBinding
    private lateinit var mItemViewModel : ItemViewModel
    private val mGoneItemAdapter by lazy{ GoneItemAdapter()}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentDuedateCheckBinding.inflate(layoutInflater, container, false)
        initViewModel()
        initRecyclerView()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
            tvToday.text = current.format(formatter)
        }
    }
    override fun onStart(){
        super.onStart()
        mItemViewModel.goneItemsFetch(binding.tvToday.text.toString())
    }

    private fun initViewModel(){
        mItemViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(ItemViewModel::class.java)
    }
    private fun initRecyclerView(){
        binding.apply{
            goneItems.adapter = mGoneItemAdapter

            mGoneItemAdapter.setGoneItemClickListener(object : GoneItemAdapter.OnGoneItemClickListener {
                override fun onClick(item : Item) {
                    showDialog(item)
                }
            })

            val layout = LinearLayoutManager(context)
            goneItems.layoutManager = layout
            goneItems.setHasFixedSize(true)
        }
        mItemViewModel.goneItems.observe(viewLifecycleOwner){
            mGoneItemAdapter.submitList(it)
        }
    }
    private fun showDialog(item : Item){
        val dialog = AlertDialog.Builder(requireContext())
        dialog.apply{
            setMessage("날짜를 초기화 하시겠습니까?")
            setPositiveButton("확인") { dialog, btnType ->
                when (btnType) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        mItemViewModel.update(item.id,item.itemName,item.location,"")
                        message("체크 완료")
                        dialog?.dismiss()
                    }
                    else -> dialog?.dismiss()
                }
            }
            setNegativeButton("취소",null)
            show()
        }
        mItemViewModel.fetchItems("")
    }
    private fun message(s : String){
        Toast.makeText(requireContext(),s,Toast.LENGTH_SHORT).show()
    }
}