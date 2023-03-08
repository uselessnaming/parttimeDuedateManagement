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
    private val mActivity by lazy {activity as MainActivity}
    private val current = LocalDateTime.now()
    private val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
    private val currentDate = current.format(formatter)

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
                    val dialog = DuedateCheckDialog()
                    dialog.setOnButtonClickListener(object : DuedateCheckDialog.OnButtonClickListener{
                        override fun onSoldOutClick() {
                            mItemViewModel.updateIsEmpty(item.id, true)
                            mItemViewModel.update(item.id,item.itemName,item.location,"")
                            mItemViewModel.goneItemsFetch(currentDate)
                            dialog.dismiss()
                        }
                        override fun onUpdateDateClick() {
                            val updateDialog = UpdateDialog()

                            updateDialog.setOnDoneClickListener(object : UpdateDialog.OnDoneClickListener{
                                override fun onClick(itemId: Int, type : String, name : String, date : String) {
                                    mItemViewModel.update(itemId, name, type, date)
                                    mItemViewModel.goneItemsFetch(currentDate)
                                    updateDialog.dismiss()
                                }
                            })
                            mActivity.createDialog(updateDialog,item.id,"UpdateDialog")
                            dialog.dismiss()
                        }
                    })
                    mActivity.createDialog(dialog,"DuedateCheckDialog")
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
    private fun message(s : String){
        Toast.makeText(requireContext(),s,Toast.LENGTH_SHORT).show()
    }
}