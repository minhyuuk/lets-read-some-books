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
import com.project.book.R
import com.project.book.base.BaseFragment
import com.project.book.databinding.FragmentSettingsBinding
import com.project.book.view.login.LoginActivity

class SettingsFragment : BaseFragment<FragmentSettingsBinding>(R.layout.fragment_settings) {
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signOut()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return binding.root
    }

    private fun signOut() {
        binding.logOut.setOnClickListener{
            auth?.signOut()
            activity?.let {
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
                Log.d("setting fragment","sign out")
            }

        }
    }
}