package com.project.book.view.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.project.book.R
import com.project.book.base.BaseActivity
import com.project.book.databinding.ActivityMainBinding
import com.project.book.view.home.HomeFragment
import com.project.book.view.profile.ProfileFragment
import com.project.book.view.search.SearchActivity
import com.project.book.view.settings.SettingsFragment

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val homeFragment by lazy { HomeFragment() }
    private val profileFragment by lazy { ProfileFragment() }
    private val settingsFragment by lazy { SettingsFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initNavigationView()
        onMenuClicked()
    }

    private fun initNavigationView() {
        binding.bnvMain.run {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.my_home -> {
                        changeFragment(homeFragment)
                    }
                    R.id.my_profile -> {
                        changeFragment(profileFragment)
                    }
                    R.id.my_settings ->{
                        changeFragment(settingsFragment)
                    }
                }
                true
            }
            selectedItemId = R.id.my_home
        }
    }
    private fun onMenuClicked(){
        binding.toolBar.title = "책좀 보자"
        binding.toolBar.setOnMenuItemClickListener{
            when(it.itemId){
                R.id.tool_search->{
                    startActivity(Intent(this, SearchActivity::class.java))
                    Log.d("MainActivity","search clicked")
                    true
                }
                R.id.tool_home->{
                    changeFragment(homeFragment)
                    Log.d("MainActivity","home clicked")
                    true
                }
                R.id.tool_profile->{
                    changeFragment(profileFragment)
                    Log.d("MainActivity","profile clicked")
                    true
                }
                R.id.tool_settings->{
                    changeFragment(settingsFragment)
                    Log.d("MainActivity","settings clicked")
                    true
                }
                else -> false
            }
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fl_container, fragment).commit()
    }

}