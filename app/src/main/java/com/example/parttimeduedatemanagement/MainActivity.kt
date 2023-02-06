package com.example.parttimeduedatemanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.part_timedatemanagement.Database.Item
import com.example.parttimeduedatemanagement.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MainActivity : AppCompatActivity() {
    private val TAG : String = "ActivityMain"
    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainerView,HomeFragment())
            .commit()

        Log.d(TAG, "Success")
    }
    /* fragment를 switch해주는 함수 */
    fun fragmentChange(currentLayoutId : Int, changedLayout : Fragment){
        supportFragmentManager.beginTransaction()
            .replace(currentLayoutId,changedLayout)
            .addToBackStack(null) //뒤로 가기 버튼 누를 시 fragment를 꺼줌
            .commit()
    }

    fun createBottomDialog(b : BottomSheetDialogFragment, item : Item){
        val bundle = Bundle()
        bundle.putInt("mainId", item.id)
        b.arguments = bundle
        b.show(supportFragmentManager, b.tag)
    }

    fun createDialog(d : DialogFragment, itemId : Int){
        val bundle = Bundle()
        bundle.putInt("itemId", itemId)
        d.arguments = bundle
        d.show(supportFragmentManager, d.tag)
    }
}