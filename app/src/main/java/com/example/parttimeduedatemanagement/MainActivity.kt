package com.example.parttimeduedatemanagement

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.parttimeduedatemanagement.Database.Item
import com.example.parttimeduedatemanagement.Memo.AddMemoFragment
import com.example.parttimeduedatemanagement.Memo.MemoFragment
import com.example.parttimeduedatemanagement.Memo.UpdateMemoFragment
import com.example.parttimeduedatemanagement.ViewModel.ItemViewModel
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

        /* ?????? ?????? ??? menu drawer ?????? */
        btnMenu = binding.root.findViewById(R.id.btnMenu)
        drawerLayout = binding.mainDrawerLayout

        btnMenu.setOnClickListener{
            if(!drawerLayout.isDrawerOpen(GravityCompat.END)) drawerLayout.openDrawer(GravityCompat.END)
        }

        /* toolbar menu ?????? */
        binding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                android.R.id.home -> {
                    Log.d(TAG,"Home")
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
                        setMessage("?????? ????????? ???????????????????")
                        setPositiveButton("??????") { _, _ ->
                            mItemViewModel.deleteAll()
                            mItemViewModel.fetchItems("")
                        }
                        setNegativeButton("??????", null)
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
                    switchFragment(CountInventoryFragment())
                    isHomeFragment = false
                    drawerLayout.closeDrawer(GravityCompat.END)
                    true
                }
                R.id.memo -> {
                    switchFragment(MemoFragment())
                    isHomeFragment = false
                    drawerLayout.closeDrawer(GravityCompat.END)
                    true
                }
                R.id.setting -> {
                    message("??? ????????? ?????????????????? ><")
                    drawerLayout.closeDrawer(GravityCompat.END)
                    true
                }
                else -> {
                    throw IllegalStateException("Error : this menu is not exist")
                }
            }
        }
    }
    /* fragment??? switch????????? ?????? */
    fun switchFragment(fragment : Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        if (curFragment == fragment) {
            message("?????? ????????? ????????? ????????????.\n?????? ????????? ????????? ?????????")
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
        val bundle = Bundle()
        bundle.putInt("itemId", itemId)
        d.arguments = bundle
        d.show(supportFragmentManager, tag)
    }
    fun createDialog(d : DialogFragment, tag : String){
        d.show(supportFragmentManager, tag)
    }
    private fun message(s : String){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show()
    }
    private fun initViewModel(){
        mItemViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
            .create(ItemViewModel::class.java)
    }
    override fun onBackPressed() {
        /* ?????? UpdateMemoFragment ?????? ???????????? ?????? ??? MemoFragment??? ??????*/
        if (curFragment is UpdateMemoFragment || curFragment is AddMemoFragment){
            switchFragment(MemoFragment())
            return
        }
        /* drawer??? ???????????? ??? -> ?????? */
        if (binding.mainDrawerLayout.isDrawerOpen(GravityCompat.END)){
            binding.mainDrawerLayout.closeDrawer(GravityCompat.END)
            return
        }
        /* HomeFragment?????? ???????????? 2?????? ????????? ??? ?????? */
        if (isHomeFragment){
            if (System.currentTimeMillis() - lastTimeBackPressed < 1500){
                finish()
                return
            }
            lastTimeBackPressed = System.currentTimeMillis()
            message("??? ??? ??? ?????? ????????? ????????? ???????????????")
            return
        }
        /* HomeFragment??? ?????? ??? ???????????? -> HomeFragment??? ?????? */
        else {
            switchFragment(HomeFragment())
            isHomeFragment = true
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> false
        }
    }
}