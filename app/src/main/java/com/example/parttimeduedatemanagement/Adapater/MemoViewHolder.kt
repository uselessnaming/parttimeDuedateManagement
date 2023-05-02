package com.example.parttimeduedatemanagement.Adapater

import androidx.recyclerview.widget.RecyclerView
import com.example.parttimeduedatemanagement.MemoDatabase.Memo
import com.example.parttimeduedatemanagement.databinding.MemoItemBinding

class MemoViewHolder(private var binding : MemoItemBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(memo : Memo){
        binding.tvTitle.text = memo.title
        binding.tvUpdateDate.text = memo.date
    }
}