package com.project.book.view.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.project.book.R
import com.project.book.base.BaseFragment
import com.project.book.databinding.ActivitySearchBinding
import com.project.book.databinding.FragmentSettingsBinding
import com.project.book.view.login.LoginActivity

class SettingsFragment : BaseFragment<FragmentSettingsBinding>(R.layout.fragment_settings) {

    private var auth: FirebaseAuth? = null

    override fun FragmentSettingsBinding.onViewCreated() {
        signOut()
        Log.e("SettingsFragment",Firebase.auth.currentUser?.email.toString())
        auth = Firebase.auth

    }

    override fun FragmentSettingsBinding.onCreateView() {
        activity?.let {
            binding.userEmail.text = Firebase.auth.currentUser?.email
        }
    }

    private fun signOut() {
        binding.logOut.setOnClickListener{
            auth?.signOut()
            activity?.let {
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
                activity?.overridePendingTransition(0,0)
                Log.d("setting fragment","sign out")
            }
        }
    }
}