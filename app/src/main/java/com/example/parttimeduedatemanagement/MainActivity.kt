package com.example.parttimeduedatemanagement

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.part_timedatemanagement.Database.Item
import com.example.part_timedatemanagement.ItemViewModel
import com.example.parttimeduedatemanagement.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MainActivity : AppCompatActivity() {

    private val TAG = "ActivityMain"
    private var lastTimeBackPressed : Long = 0
    private lateinit var binding : ActivityMainBinding
    private lateinit var toolbar : Toolbar
    private lateinit var mItemViewModel : ItemViewModel
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var btnMenu : ImageButton
    private var curFragment : Fragment? = null
    private var isHomeFragment : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        toolbar = findViewById(R.id.toolbar)
        toolbar.setTitle(R.string.app_name)
        setSupportActionBar(toolbar)

        switchFragment(HomeFragment())
        isHomeFragment = true

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        initViewModel()

        /* 버튼 클릭 시 menu drawer 열림 */
        btnMenu = binding.root.findViewById(R.id.btnMenu)
        drawerLayout = binding.mainDrawerLayout

        btnMenu.setOnClickListener{
            if(!drawerLayout.isDrawerOpen(GravityCompat.END)) drawerLayout.openDrawer(GravityCompat.END)
        }

        /* toolbar menu 설정 */
        binding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                android.R.id.home -> {
                    onBackPressed()
                    true
                }
                R.id.insert -> {
                    switchFragment(InsertFragment())
                    isHomeFragment = false
                    drawerLayout.closeDrawer(GravityCompat.END)
                    true
                }
                R.id.checkDuedate -> {
                    switchFragment(DuedateCheckFragment())
                    isHomeFragment = false
                    drawerLayout.closeDrawer(GravityCompat.END)
                    true
                }
                R.id.reset -> {
                    val builder = AlertDialog.Builder(this@MainActivity)
                    builder.apply {
                        setTitle("Warning")
                        setMessage("정말 초기화 하시겠습니까?")
                        setPositiveButton("확인") { _, _ ->
                            mItemViewModel.deleteAll()
                            mItemViewModel.fetchItems("")
                        }
                        setNegativeButton("취소", null)
                        show()
                    }
                    drawerLayout.closeDrawer(GravityCompat.END)
                    true
                }
                R.id.editType -> {
                    switchFragment(TypeEditFragment())
                    isHomeFragment = false
                    drawerLayout.closeDrawer(GravityCompat.END)
                    true
                }
                R.id.countInventory -> {

                    isHomeFragment = false
                    drawerLayout.closeDrawer(GravityCompat.END)
                    true
                }
                else -> {
                    throw IllegalStateException("Error : this menu is not exist")
                }
            }
        }
    }
    /* fragment를 switch해주는 함수 */
    private fun switchFragment(fragment : Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        if (curFragment == fragment) {
            message("현재 선택된 화면에 있습니다.\n다른 메뉴를 선택해 주세요")
            return
        }
        if (curFragment != null) {
            transaction.replace(R.id.fragmentContainerView, fragment)
                .setReorderingAllowed(true)
        } else {
            transaction.add(R.id.fragmentContainerView, fragment)
        }
        transaction.commit()
        curFragment = fragment
    }

    fun createBottomDialog(b : BottomSheetDialogFragment, item : Item){
        val bundle = Bundle()
        bundle.putInt("mainId", item.id)
        b.arguments = bundle
        b.show(supportFragmentManager, b.tag)
    }

    fun createDialog(d : DialogFragment, itemId : Int, tag : String){
        if (tag == "typeEditDialog"){
            d.show(supportFragmentManager,tag)
        } else if (tag == "updateDialog"){
            val bundle = Bundle()
            bundle.putInt("itemId", itemId)
            d.arguments = bundle
            d.show(supportFragmentManager, tag)
        }

    }

    private fun message(s : String){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show()
    }

    private fun initViewModel(){
        mItemViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
            .create(ItemViewModel::class.java)
    }

    override fun onBackPressed() {
        /* drawer가 열려있을 때 -> 닫음 */
        if (binding.mainDrawerLayout.isDrawerOpen(GravityCompat.END)){
            binding.mainDrawerLayout.closeDrawer(GravityCompat.END)
            return
        }
        /* HomeFragment라면 뒤로가기 2번을 눌렀을 때 종료 */
        if (isHomeFragment){
            if (System.currentTimeMillis() - lastTimeBackPressed < 1500){
                finish() //finish()?
                return
            }
            lastTimeBackPressed = System.currentTimeMillis()
            message("한 번 더 뒤로 가기를 누르면 종료됩니다")
            return
        }
        /* HomeFragment가 아닐 때 뒤로가기 -> HomeFragment로 전환 */
        else {
            switchFragment(HomeFragment())
            isHomeFragment = true
        }
    }
}