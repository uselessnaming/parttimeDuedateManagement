package com.example.parttimeduedatemanagement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.part_timedatemanagement.Base.BaseFragment

//menu에서 insert 클릭 시
class InsertFragment : BaseFragment() {
    private val TAG : String = "InsertFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_insert, container, false)
    }
}