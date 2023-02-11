package com.example.parttimeduedatemanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.part_timedatemanagement.Database.Item
import com.example.parttimeduedatemanagement.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MainActivity : AppCompatActivity() {
    private val TAG = "ActivityMain"
    private var lastTimeBackPressed : Long = 0
    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainerView,HomeFragment())
            .commit()
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
    private fun message(s : String){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        val currentFragment : Fragment = supportFragmentManager.fragments[0]
        if (currentFragment is HomeFragment){
            if (System.currentTimeMillis() - lastTimeBackPressed < 1500){
                finish()
                return
            }
            lastTimeBackPressed = System.currentTimeMillis()
            message("한 번 더 뒤로 가기를 누르면 종료됩니다")
        } else{
            super.onBackPressed()
        }
    }

    /*
    fun finishFragment(){
        supportFragmentManager.popBackStack()
    }
    */
}