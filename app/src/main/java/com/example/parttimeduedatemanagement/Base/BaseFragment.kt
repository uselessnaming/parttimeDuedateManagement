package com.example.part_timedatemanagement.Base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    override fun onViewCreated(view : View, savedInstanceState : Bundle?){
        super.onViewCreated(view,savedInstanceState)
    }
}