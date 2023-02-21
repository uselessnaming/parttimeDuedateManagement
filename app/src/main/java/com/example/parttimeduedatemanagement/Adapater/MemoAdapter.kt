package com.example.parttimeduedatemanagement.Adapater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.parttimeduedatemanagement.Database.CheckItemList
import com.example.parttimeduedatemanagement.MemoDatabase.Memo
import com.example.parttimeduedatemanagement.databinding.MemoItemBinding

class MemoAdapter : RecyclerView.Adapter<MemoAdapter.MemoViewHolder>() {
    private val TAG = "MemoAdapter"
    private val memos = arrayListOf<Memo>()
    private lateinit var binding : MemoItemBinding

    fun submitList(memos : List<Memo>){
        this.memos.clear()
        this.memos.addAll(memos)
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = this.memos.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType : Int) : MemoViewHolder{
        binding = MemoItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MemoViewHolder(binding)
    }
    override fun onBindViewHolder(holder : MemoViewHolder, position : Int){
        holder.bind(memos[position])
    }
    inner class MemoViewHolder(binding : MemoItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(memo : Memo){
            binding.tvTitle.text = memo.title
            binding.tvUpdateDate.text = memo.date
        }
    }
}