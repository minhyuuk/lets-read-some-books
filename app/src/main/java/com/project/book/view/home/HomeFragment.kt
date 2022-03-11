package com.project.book.view.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.project.book.R
import com.project.book.base.BaseFragment
import com.project.book.databinding.ActivityMainBinding
import com.project.book.databinding.FragmentHomeBinding
import com.project.book.view.main.MainActivity
import com.project.book.view.profile.ProfileFragment
import com.project.book.view.search.SearchActivity
import com.project.book.view.settings.SettingsFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val profileFragment by lazy { ProfileFragment() }
    private val settingsFragment by lazy { SettingsFragment() }

    override fun FragmentHomeBinding.onViewCreated() {}
    override fun FragmentHomeBinding.onCreateView() {}

    private fun changeFragment(fragment: Fragment) {
        (activity as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.fl_container, fragment).commit()
    }


}