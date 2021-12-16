package com.project.book.view.home

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import com.project.book.R
import com.project.book.base.BaseFragment
import com.project.book.databinding.FragmentHomeBinding
import com.project.book.view.profile.ProfileFragment
import com.project.book.view.search.SearchActivity
import com.project.book.view.settings.SettingsFragment
import kotlin.concurrent.fixedRateTimer

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    override fun FragmentHomeBinding.onViewCreated() {
        binding.profileImg.setOnClickListener {
            fragmentManager
                ?.beginTransaction()
                ?.replace(R.id.fl_container,ProfileFragment())
                ?.commit()
        }
        binding.searchImg.setOnClickListener {
            val intent = Intent(activity,SearchActivity::class.java)
            startActivity(intent)
        }
        binding.settingsImg.setOnClickListener {
            fragmentManager
                ?.beginTransaction()
                ?.replace(R.id.fl_container,SettingsFragment())
                ?.commit()
        }
    }

    override fun FragmentHomeBinding.onCreateView() {

    }
}