package com.example.parttimeduedatemanagement

import android.app.ProgressDialog.show
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.core.view.GravityCompat
import com.example.parttimeduedatemanagement.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val TAG : String = "ActivityMain"
    private lateinit var binding : ActivityMainBinding
    private lateinit var flag : String // 수정, 추가 를 구분시킬 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "Success")
    }

    /** menu 설정 */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return true
    }

    /** menu의 옵션 선택 시 */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            Menu.FIRST + 1 -> {
                flag = "insert"

            }
            Menu.FIRST + 2 -> {
                flag = "update"
            }
        }
        return super.onOptionsItemSelected(item)
    }
}