package com.project.book.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.project.book.R
import com.project.book.base.BaseActivity
import com.project.book.databinding.ActivityLoginBinding
import com.project.book.util.Extensions.toast
import com.project.book.view.login.signup.SignupActivity
import com.project.book.view.main.MainActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private lateinit var client: GoogleSignInClient
    private lateinit var  auth: FirebaseAuth
    private var backTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        login()
        nextPage()
    }


    private fun loginLogic() {
        val email = binding.textInputTextEmail.text.toString()
        val password = binding.textInputTextPassword.text.toString()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                toast("로그인에 성공했습니다")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun login(){
        binding.emailLoginButton.setOnClickListener{
            loginLogic()
        }
    }
    private fun nextPage(){
        binding.signupText.setOnClickListener{
            startActivity(Intent(this,SignupActivity::class.java))
            overridePendingTransition(0,0)
            finish()
        }
    }

    override fun onBackPressed() {
        Log.d("LoginActivity - 뒤로가기","뒤로가기")

        if(System.currentTimeMillis() - backTime < 2000){
            finish()
            return
        }
        toast("뒤로가기 버튼을 한번 더 누르면 앱이 종료됩니다.")
        backTime = System.currentTimeMillis()

    }

}