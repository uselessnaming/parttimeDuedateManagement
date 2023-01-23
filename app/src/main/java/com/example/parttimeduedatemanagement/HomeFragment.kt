package com.example.parttimeduedatemanagement

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.part_timedatemanagement.Base.BaseFragment
import com.example.part_timedatemanagement.ItemViewModel
import com.example.parttimeduedatemanagement.Adapater.ItemAdapter
import com.example.parttimeduedatemanagement.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment() {
    private val TAG = "HomeFragment"
    private lateinit var binding : FragmentHomeBinding
    private lateinit var mItemViewModel : ItemViewModel
    private val mActivity by lazy {
        activity as MainActivity
    }
    private val mItemAdapter by lazy{
        ItemAdapter()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)

        initViewModel()
        initRecyclerView()
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onStart(){
        super.onStart()
        mItemViewModel.fetchItems()
    }

    /** ViewModel 초기화 */
    private fun initViewModel(){
        mItemViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(ItemViewModel::class.java)
    }

    /** itemContainerRecyclerview를 초기화 */
    private fun initRecyclerView (){
        binding.apply{
            binding.itemList.adapter = mItemAdapter

            val layout = LinearLayoutManager(context)
            itemList.layoutManager = layout
            itemList.setHasFixedSize(true)
        }

        mItemViewModel.items.observe(viewLifecycleOwner, Observer{
            Log.d(TAG, "Observer is Playing")
            Log.d(TAG, "${it}")
            /* 중복 제거 */
            mItemAdapter.submitList(it)
        })
    }

    /** menu 생성 */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        mActivity.menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu, MenuInflater(requireContext()))
    }

    /** menu 선택 시 이벤트 처리 */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.insert -> {
                mActivity.fragmentChange(R.id.fragmentContainerView, InsertFragment())
            }
            R.id.reset -> {
                /* homeFragment에 reset을 넘겨줘서 reset을 받으면 데이터를 reset하도록 */
                val builder = AlertDialog.Builder(context)
                builder.apply {
                    setTitle("Warning")
                    setMessage("정말 초기화 하시겠습니까?")
                    setPositiveButton("확인",
                        DialogInterface.OnClickListener { dialog, id ->
                            mItemViewModel.deleteAll()
                            Log.d(TAG,"${mItemAdapter.itemCount}")
                        })
                    setNegativeButton("취소", null)
                    show()
                }
            }
            else -> {
                throw IllegalArgumentException("menu is not exist")
            }
        }
        return super.onOptionsItemSelected(item)
    }
}