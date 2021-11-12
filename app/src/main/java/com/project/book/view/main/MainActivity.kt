package com.project.book.view.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.internal.NavigationMenuItemView
import com.project.book.R
import com.project.book.databinding.ActivityMainBinding
import com.project.book.view.profile.ProfileActivity
import com.project.book.view.search.SearchActivity
import com.project.book.view.settings.SettingsActivity


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.bnvMain.setOnNavigationItemSelectedListener(this)
    }

    override fun onStart() {
        super.onStart()
//        updateNavigationBarState()
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }

    override fun onNavigationItemSelected(@NonNull item: MenuItem): Boolean {
        binding.bnvMain.postDelayed({
            val itemId = item.itemId
            if (itemId == R.id.my_search) {
                startActivity(Intent(this, SearchActivity::class.java))
            } else if (itemId == R.id.my_home) {
                startActivity(Intent(this, ProfileActivity::class.java))
            } else if (itemId == R.id.my_profile) {
                startActivity(Intent(this, ProfileActivity::class.java))
            } else if (itemId == R.id.my_settings) {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
            finish()
        }, 300)
        return true
    }

    private fun updateNavigationBarState() {
        val actionId: Int = NavigationMenuItemView.generateViewId()
        selectBottomNavigationBarItem(actionId)
    }

    private fun selectBottomNavigationBarItem(itemId: Int) {
        val item: MenuItem = binding.bnvMain.menu.findItem(itemId)
        item.isChecked = true
    }

}