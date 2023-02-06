package com.example.parttimeduedatemanagement

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.part_timedatemanagement.ItemViewModel
import com.example.parttimeduedatemanagement.Adapater.GoneItemAdapter
import com.example.parttimeduedatemanagement.databinding.FragmentBottomDialogBinding
import com.example.parttimeduedatemanagement.databinding.FragmentDuedateCheckBinding
import java.time.LocalDate
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
        Log.d(TAG,"ONcreateView is done")
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
            tvToday.text = current.format(formatter)

        }
        Log.d(TAG,"onViewCreated is done")
    }
    override fun onStart(){
        super.onStart()
        mItemViewModel.goneItemsFetch(binding.tvToday.text.toString())
        Log.d(TAG,"onStart is done")
    }

    private fun initViewModel(){
        mItemViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(ItemViewModel::class.java)
    }
    private fun initRecyclerView(){
        binding.apply{
            goneItems.adapter = mGoneItemAdapter

            mGoneItemAdapter.setGoneItemClickListener(object : GoneItemAdapter.OnGoneItemClickListener {
                override fun onClick(itemId: Int) {
                    showDialog(itemId)
                }
            })

            val layout = LinearLayoutManager(context)
            goneItems.layoutManager = layout
            goneItems.setHasFixedSize(true)
        }
        mItemViewModel.goneItems.observe(viewLifecycleOwner, Observer{
            mGoneItemAdapter.submitList(it)
            Log.d("GoneItemViewHolder","${it}")
        })
    }
    private fun showDialog(itemId : Int){
        val dialog = AlertDialog.Builder(requireContext())
        dialog.apply{
            setTitle("Warning")
            setMessage("Are you sure about deleting this item?")
            setPositiveButton("OK") { dialog, btnType ->
                when (btnType) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        mItemViewModel.deleteItem(itemId)
                        dialog?.dismiss()
                    }
                    else -> dialog?.dismiss()
                }
            }
            setNegativeButton("Cancel",null)
            show()
        }
    }
}