package com.example.parttimeduedatemanagement.Memo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.parttimeduedatemanagement.MemoDatabase.Memo
import com.example.parttimeduedatemanagement.databinding.FragmentUpdateMemoBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UpdateMemoFragment : Fragment() {

    private lateinit var binding : FragmentUpdateMemoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentUpdateMemoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply{
            val title = arguments?.getString("title") ?: throw IllegalArgumentException("Error : title 값이 전달되지 않았습니다")
            val content = arguments?.getString("content") ?: ""
            etTitle.setText(title)
            etContent.setText(content)
            btnDone.setOnClickListener {
                if (etTitle.text.isEmpty()){
                    message("제목을 써주세요")
                } else {
                    var updateContent = ""
                    if (etContent.text.isNotEmpty()){
                        updateContent = etContent.text.toString()
                    }
                    val current = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
                    val updateDate = current.format(formatter)
                    onClickListener.onDoneClick(Memo(etTitle.text.toString(),updateContent,updateDate))
                    message("수정 완료")
                }
            }
            btnCancel.setOnClickListener {
                onClickListener.onCancelClick()
            }
        }
    }
    interface OnClickListener{
        fun onDoneClick(memo : Memo)
        fun onCancelClick()
    }
    private lateinit var onClickListener : OnClickListener
    fun setOnDoneClickListener(onClickListener : OnClickListener){
        this.onClickListener = onClickListener
    }
    private fun message(s : String){
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }
}