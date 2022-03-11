package com.project.book.view.profile

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.project.book.R
import com.project.book.base.BaseFragment
import com.project.book.databinding.FragmentProfileBinding

class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    private var auth: FirebaseAuth? = null

    override fun FragmentProfileBinding.onViewCreated() {
        Log.e("ProfileFragment", Firebase.auth.currentUser?.email.toString())
        auth = Firebase.auth
    }

    override fun FragmentProfileBinding.onCreateView() {
        activity?.let {
            binding.userNickname.text = Firebase.auth.currentUser?.email + " 님!"
        }
    }

}