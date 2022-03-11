package com.project.book.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.project.book.R
import com.project.book.base.BaseActivity
import com.project.book.databinding.ActivityLoginBinding
import com.project.book.util.Extensions.toast
import com.project.book.view.login.signup.SignupActivity
import com.project.book.view.main.MainActivity

class LoginActivity() : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private lateinit var client: GoogleSignInClient
    private var auth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = Firebase.auth

        login()
        moveSignUpPage()

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onStart() {
        super.onStart()
        Log.d("LoginActivity","onStart")
        moveMainPage(auth?.currentUser)
    }

    private fun loginLogic() {
        val email = binding.textInputTextEmail.text.toString()
        val password = binding.textInputTextPassword.text.toString()
        auth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                toast("로그인에 성공했습니다")
                moveMainPage(auth?.currentUser)
            }
        }?.addOnFailureListener { exception ->
            Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun login(){
        binding.emailLoginButton.setOnClickListener{
            loginLogic()
        }
    }
    private fun moveSignUpPage(){
        binding.signupText.setOnClickListener{
            startActivity(Intent(this,SignupActivity::class.java))
            overridePendingTransition(0,0)
            finish()
        }
    }

    private fun moveMainPage(user: FirebaseUser?){
        if(user != null){
            startActivity(Intent(this,MainActivity::class.java))
            overridePendingTransition(0,0)
            finish()
        }
    }

}