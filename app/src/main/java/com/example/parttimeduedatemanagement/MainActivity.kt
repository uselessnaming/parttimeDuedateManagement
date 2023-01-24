package com.example.parttimeduedatemanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.fragment.app.Fragment
import com.example.part_timedatemanagement.Database.Item
import com.example.parttimeduedatemanagement.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val TAG : String = "ActivityMain"
    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, HomeFragment())
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
}