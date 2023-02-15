package com.example.parttimeduedatemanagement

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
                mItemViewModel.fetchItems("")
            }
            val menus = listOf("아이템 추가 순","유통기한순","이름순")
            val adapter = ArrayAdapter.createFromResource(requireContext(),R.array.sortingMenu,R.layout.choice_spinner_item)
            spSort.adapter = adapter
            spSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    when(menus[position]){
                        "아이템 추가 순" ->{mItemViewModel.fetchItems("addTime")}
                        "유통기한순" -> {mItemViewModel.fetchItems("duedate")}
                        "이름순" -> {mItemViewModel.fetchItems("name")}
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        }
    }
    override fun onStart(){
        super.onStart()
        mItemViewModel.fetchItems("")
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
                    dialog.setOnDoneClickListener(object : BottomDialog.OnDoneClickListener{
                        override fun onClick(itemId: Int,name: String,type: String,date: String,) {
                            mItemViewModel.update(itemId, name, type, date)
                            mItemViewModel.fetchItems("")
                            dialog.dismiss()
                        }
                    })
                    mActivity.createBottomDialog(dialog,id.item)
                }
            })

            val layout = LinearLayoutManager(context)
            itemList.layoutManager = layout
            itemList.setHasFixedSize(true)
            Log.d(TAG,"${mItemAdapter.itemCount}")
        }

        mItemViewModel.items.observe(viewLifecycleOwner, Observer{
            mItemAdapter.submitList(it)
            binding.itemCount.text = "등록된 상품의 개수 : " + mItemViewModel.getItemCount().toString()
        })
    }

    /** menu 생성 */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        mActivity.menuInflater.inflate(R.menu.toolbar_menu, menu)
    }

    /** menu 선택 시 이벤트 처리 */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.insert -> {
                Log.d(TAG,"menu item in INSERT")
                mActivity.fragmentChange(R.id.fragmentContainerView, InsertFragment())
                true
            }
            R.id.reset -> {
                /* homeFragment에 reset을 넘겨줘서 reset을 받으면 데이터를 reset하도록 */
                val builder = AlertDialog.Builder(context)
                builder.apply {
                    setTitle("Warning")
                    setMessage("정말 초기화 하시겠습니까?")
                    setPositiveButton("확인") { _, _ ->
                        mItemViewModel.deleteAll()
                        mItemViewModel.fetchItems("")
                    }
                    setNegativeButton("취소", null)
                    show()
                }
                true
            }
            R.id.checkDuedate -> {
                mActivity.fragmentChange(R.id.fragmentContainerView, DuedateCheckFragment())
                true
            }
            R.id.editType -> {
                mActivity.fragmentChange(R.id.fragmentContainerView, TypeEditFragment())
                true
            }
            else -> {
                throw IllegalArgumentException("menu is not exist")
            }
        }
    }
}