package com.example.parttimeduedatemanagement

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parttimeduedatemanagement.ViewModel.ItemViewModel
import com.example.parttimeduedatemanagement.Adapater.ItemAdapter
import com.example.parttimeduedatemanagement.Adapater.ItemChildViewHolder
import com.example.parttimeduedatemanagement.Database.CheckItemList
import com.example.parttimeduedatemanagement.Event.HoldableSwipeHelper
import com.example.parttimeduedatemanagement.Event.SwipeButtonAction
import com.example.parttimeduedatemanagement.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {
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
    @SuppressLint("ClickableViewAccessibility")
    private fun initRecyclerView (){
        mItemViewModel.items.observe(viewLifecycleOwner){
            mItemAdapter.submitList(it)
            binding.itemCount.text = "등록된 상품의 개수 : " + mItemViewModel.getItemCount().toString()
        }
        binding.apply{
            /* swipe 이벤트 적용 */
            HoldableSwipeHelper.Builder(requireContext())
                .setOnRecyclerView(itemList)
                .setSwipeButtonAction(object : SwipeButtonAction{
                    override fun onClickFirstButton(viewHolder: RecyclerView.ViewHolder) {
                        val currentViewHolder = (viewHolder as ItemChildViewHolder)
                        val id = currentViewHolder.getBindingId()
                        mItemViewModel.viewModelScope.launch(Dispatchers.IO){
                            val item = mItemViewModel.searchItem(id).await()
                            withContext(Dispatchers.Main){
                                currentViewHolder.setImageTag(item.isEmpty)
                            }
                            mItemViewModel.updateIsEmpty(item.id)
                        }
                    }
                })
                .setDismissOnClickFirstItem(true)
                .excludeFromHoldableViewHolder(200)
                .setBackgroundColor("#ff0000")
                .setDirectionAsRightToLeft(false)
                .build()

            itemList.apply{
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                layoutManager = LinearLayoutManager(context)
                adapter = mItemAdapter
                setHasFixedSize(true)
            }
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
        }
    }
}