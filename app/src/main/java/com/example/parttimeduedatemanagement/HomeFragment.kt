package com.example.parttimeduedatemanagement

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.part_timedatemanagement.Base.BaseFragment
import com.example.part_timedatemanagement.ItemViewModel
import com.example.parttimeduedatemanagement.Adapater.ItemAdapter
import com.example.parttimeduedatemanagement.Database.CheckItemList
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply{
            btnRefresh.setOnClickListener{
                mItemViewModel.fetchItems()
            }
        }
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
            itemList.adapter = mItemAdapter
            mItemAdapter.setItemLongClickListener(object : ItemAdapter.OnItemLongClickListener{
                override fun onLongClick(v: View, id : CheckItemList) {
                    val dialog = BottomDialog()
                    /* BottomDialogFragment 생성 */
                    mActivity.createBottomDialog(dialog,id.item)
                }
            })

            val layout = LinearLayoutManager(context)
            itemList.layoutManager = layout
            itemList.setHasFixedSize(true)
            Log.d(TAG,"${mItemAdapter.itemCount}")
        }

        mItemViewModel.items.observe(viewLifecycleOwner, Observer{
            Log.d(TAG, "Observer is Playing")
            Log.d(TAG, "${it}")
            mItemAdapter.submitList(it)
            binding.itemCount.text = "등록된 상품의 개수 : " + mItemViewModel.getItemCount().toString()
        })
    }

    /** menu 생성 */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        mActivity.menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu, MenuInflater(requireContext()))
    }

    /** menu 선택 시 이벤트 처리 */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.insert -> {
                mActivity.fragmentChange(R.id.fragmentContainerView, InsertFragment())
            }
            R.id.reset -> {
                /* homeFragment에 reset을 넘겨줘서 reset을 받으면 데이터를 reset하도록 */
                val builder = AlertDialog.Builder(context)
                builder.apply {
                    setTitle("Warning")
                    setMessage("정말 초기화 하시겠습니까?")
                    setPositiveButton("확인") { dialog, id ->
                        mItemViewModel.deleteAll()
                        Log.d(TAG, "${mItemAdapter.itemCount}")
                        mItemAdapter.submitList(emptyList())
                    }
                    setNegativeButton("취소", null)
                    show()
                }
            }
            R.id.checkDuedate -> {
                mActivity.fragmentChange(R.id.fragmentContainerView, DuedateCheckFragment())
            }
            else -> {
                throw IllegalArgumentException("menu is not exist")
            }
        }
        return super.onOptionsItemSelected(item)
    }
}