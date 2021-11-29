package com.project.book.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.project.book.view.main.MainActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private lateinit var client: GoogleSignInClient
    private lateinit var  auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setGoogleLogin()

        auth = FirebaseAuth.getInstance()
        login()
    }

    override fun onStart() {
        super.onStart()
         val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }

    private fun setGoogleLogin(){
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail().build()
        client = GoogleSignIn.getClient(this, options)

        binding.googleLoginButton.setOnClickListener{
            startActivityForResult(client.signInIntent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            var account: GoogleSignInAccount? = null
            try {
                account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!.idToken)
            } catch (e: ApiException) {
                toast("구글 로그인에 실패했습니다")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        // 인증에 성공한 후, 현재 로그인된 유저의 정보를 가져올 수 있다.
                        val email = auth.currentUser?.email
                    }
                })
    }
    private fun loginLogic(){
        val email=binding.textInputTextEmail.text.toString()
        val password=binding.textInputTextPassword.text.toString()
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val intent= Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext,exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun login(){
        binding.emailLoginButton.setOnClickListener{
            loginLogic()
        }
    }

}