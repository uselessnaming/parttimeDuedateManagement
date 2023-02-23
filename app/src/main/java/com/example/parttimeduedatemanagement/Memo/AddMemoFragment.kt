package com.example.parttimeduedatemanagement.Memo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.parttimeduedatemanagement.MemoDatabase.Memo
import com.example.parttimeduedatemanagement.R
import com.example.parttimeduedatemanagement.databinding.FragmentAddMemoBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddMemoFragment : Fragment() {

    private lateinit var binding : FragmentAddMemoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddMemoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply{
            btnDone.setOnClickListener{
                if (etTitle.text.isEmpty()){
                    message("제목을 채워주세요")
                } else {
                    val title = etTitle.text.toString()
                    var content = ""
                    if (etContent.text.isNotEmpty()) {
                        content = etContent.text.toString()
                    }
                    val current = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
                    val date = current.format(formatter)
                    onDoneClickListener.onDoneClick(Memo(title,content,date))
                    message("추가 완료")
                }
            }
        }
    }
    interface OnDoneClickListener{
        fun onDoneClick(memo : Memo)
    }
    private lateinit var onDoneClickListener : OnDoneClickListener
    fun setOnDoneClickListener(onDoneClickListener : OnDoneClickListener){
        this.onDoneClickListener = onDoneClickListener
    }
    private fun message(s : String){
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }
}