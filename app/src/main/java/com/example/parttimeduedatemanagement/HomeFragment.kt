package com.example.parttimeduedatemanagement

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.parttimeduedatemanagement.ViewModel.ItemViewModel
import com.example.parttimeduedatemanagement.Adapater.ItemAdapter
import com.example.parttimeduedatemanagement.Database.Item
import com.example.parttimeduedatemanagement.ViewModel.MemoViewModel
import com.example.parttimeduedatemanagement.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"
    private lateinit var binding : FragmentHomeBinding
    private lateinit var mItemViewModel : ItemViewModel
    private lateinit var mMemoViewModel : MemoViewModel
    private var isReset = false
    private val current = LocalDateTime.now()
    private val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
    private val currentDate = current.format(formatter)
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

        initItemViewModel()
        initMemoViewModel()
        initRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /* view create 당시 재고파악 memo의 date와 현재 날짜가 다르다면 memo.delete */
        mMemoViewModel.viewModelScope.launch(Dispatchers.IO){
            val isTitle = mMemoViewModel.isTitle("재고 파악").await()
            if (isTitle) {
                val memoId = mMemoViewModel.searchId("재고 파악").await()
                val memo = mMemoViewModel.searchMemo(memoId).await()
                if (!currentDate.equals(memo.date)){
                    mMemoViewModel.deleteMemo(memoId)
                    mMemoViewModel.fetchMemos()
                    setResetFlag(true)
                }
            }
            if (isReset){
                mItemViewModel.resetEA()
                setResetFlag(false)
            }
        }
        binding.apply{
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
            homeFragment.setOnRefreshListener {
                onStart()
                message("새로고침 완료")
                homeFragment.isRefreshing = false
            }
        }
    }

    override fun onStart(){
        super.onStart()
        mItemViewModel.fetchItems("")
    }

    /** ViewModel 초기화 */
    private fun initItemViewModel(){
        mItemViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(ItemViewModel::class.java)
    }
    private fun initMemoViewModel(){
        mMemoViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(MemoViewModel::class.java)
    }

    /** itemContainerRecyclerview를 초기화 */
    @SuppressLint("ClickableViewAccessibility")
    private fun initRecyclerView (){
        mItemViewModel.items.observe(viewLifecycleOwner){
            Log.d("BBBBB","${it}")
            mItemAdapter.submitList(it)
            binding.itemCount.text = "등록된 상품의 개수 : " + mItemViewModel.getItemCount().toString()
        }
        binding.apply{
            /* swipe 이벤트 적용 */
            itemList.apply{
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                layoutManager = LinearLayoutManager(context)
                adapter = mItemAdapter
                setHasFixedSize(true)
            }
            mItemAdapter.setItemLongClickListener(object : ItemAdapter.OnItemLongClickListener{
                override fun onLongClick(v: View, item : Item) {
                    val dialog = BottomDialog()
                    /* BottomDialogFragment 생성 */
                    dialog.setOnDoneClickListener(object : BottomDialog.OnDoneClickListener{
                        override fun onClick(itemId: Int,name: String,type: String,date: String,) {
                            mItemViewModel.update(itemId, name, type, date)
                            mItemViewModel.fetchItems("")
                            dialog.dismiss()
                        }
                    })
                    mActivity.createBottomDialog(dialog,item)
                }
            })
        }
    }
    private fun setResetFlag(isReset : Boolean){
        this.isReset = isReset
    }
    private fun message(s : String){
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show()
    }
}