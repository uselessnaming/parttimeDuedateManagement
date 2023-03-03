package com.example.parttimeduedatemanagement.Memo

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.parttimeduedatemanagement.Adapater.MemoAdapter
import com.example.parttimeduedatemanagement.MainActivity
import com.example.parttimeduedatemanagement.MemoDatabase.Memo
import com.example.parttimeduedatemanagement.ViewModel.MemoViewModel
import com.example.parttimeduedatemanagement.databinding.FragmentMemoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MemoFragment : Fragment() {

    private val TAG = "MemoFragment"
    private lateinit var binding : FragmentMemoBinding
    private lateinit var mMemoViewModel : MemoViewModel
    private val mMemoAdapter by lazy{ MemoAdapter() }
    private val mActivity by lazy{activity as MainActivity}
    private val current = LocalDateTime.now()
    private val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
    private val currentDate = current.format(formatter)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMemoBinding.inflate(layoutInflater, container, false)

        initViewModel()
        initRecyclerView()
        mMemoViewModel.fetchMemos()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgPlus.setOnClickListener{
            /* AddMemoFragment 띄우기 */
            val addMemoFragment = AddMemoFragment()
            addMemoFragment.setOnDoneClickListener(object : AddMemoFragment.OnDoneClickListener{
                override fun onDoneClick(memo : Memo) {
                    mMemoViewModel.insertMemo(memo)
                    mMemoViewModel.fetchMemos()
                    mActivity.switchFragment(MemoFragment())
                }
            })
            mActivity.switchFragment(addMemoFragment)
        }
        binding.btnRefresh.setOnClickListener{ 
            mMemoViewModel.fetchMemos()
            message("새로 고침 완료")
        }
    }

    override fun onStart() {
        super.onStart()
        val content = arguments?.getString("content") ?: ""
        mMemoViewModel.viewModelScope.launch(Dispatchers.IO){
            val isTitle = mMemoViewModel.isTitle("재고 파악").await()
            if (content != "") {
                if (isTitle) {
                    val id = mMemoViewModel.searchId("재고 파악").await()
                    mMemoViewModel.updateMemo(id, Memo("재고 파악", content, currentDate))
                    mMemoViewModel.fetchMemos()
                } else {
                    mMemoViewModel.insertMemo(Memo("재고 파악", content, currentDate))
                    mMemoViewModel.fetchMemos()
                }
            }
        }
    }
    private fun initViewModel(){
        mMemoViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(MemoViewModel::class.java)
    }
    private fun initRecyclerView(){
        binding.apply{
            rcvMemoTitle.adapter = mMemoAdapter
            mMemoAdapter.setOnClickListener(object : MemoAdapter.OnClickListener{
                override fun onClick(memo : Memo) {
                    val updateMemoFragment = UpdateMemoFragment()
                    val id = memo.id
                    updateMemoFragment.setOnDoneClickListener(object : UpdateMemoFragment.OnClickListener{
                        override fun onDoneClick(memo: Memo) {
                            mMemoViewModel.updateMemo(id, memo)
                            mMemoViewModel.fetchMemos()
                            mActivity.switchFragment(MemoFragment())
                        }
                        override fun onCancelClick() {
                            mActivity.switchFragment(MemoFragment())
                        }
                    })
                    val bundle = Bundle()
                    bundle.putString("title",memo.title)
                    bundle.putString("content",memo.content)
                    updateMemoFragment.arguments = bundle
                    mActivity.switchFragment(updateMemoFragment)
                }
            })
            mMemoAdapter.setOnLongClickListener(object : MemoAdapter.OnLongClickListener{
                override fun onLongClick(id : Int) {
                    val dialog = AlertDialog.Builder(context)
                    val listener = DialogInterface.OnClickListener { _, p1 ->
                        when(p1){
                            DialogInterface.BUTTON_POSITIVE -> {
                                mMemoViewModel.deleteMemo(id)
                                mMemoViewModel.fetchMemos()
                                message("삭제 완료")
                            }
                        }
                    }
                    dialog.apply{
                        setTitle("주의")
                        setMessage("정말 삭제하시겠습니까?")
                        setPositiveButton("확인",listener)
                        setNegativeButton("취소",null)
                        show()
                    }
                }
            })
            val layoutManager = LinearLayoutManager(context)
            rcvMemoTitle.layoutManager = layoutManager
            rcvMemoTitle.setHasFixedSize(true)
        }
        mMemoViewModel.memos.observe(viewLifecycleOwner){
            mMemoAdapter.submitList(it)
        }
    }
    private fun message(s : String){
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }
}