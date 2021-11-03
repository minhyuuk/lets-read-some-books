package com.project.book.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.project.book.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private lateinit var client: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    private val user = FirebaseAuth.getInstance().currentUser
    private val email = user?.email
    private val name = user?.displayName
    private val photoUrl = user?.photoUrl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        emailTextWatcher()
//        passwordTextWatcher()
        setGoogleLogin()
    }

    private fun emailTextWatcher() {
        binding.textInputTextEmail.addTextChangedListener(object : TextWatcher{

            override fun afterTextChanged(p0: Editable?) {
//                if(binding.textInputTextEmail.text!!.isEmpty())
//                    binding.textInputLayoutEmail.error = "올바른 입력 값이 아닙니다!"
//                else{
//
//                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    private fun passwordTextWatcher() {
        binding.textInputTextPassword.addTextChangedListener(object : TextWatcher{

            override fun afterTextChanged(p0: Editable?) {
//                if(binding.textInputTextPassword.text!!.isEmpty())
//                    binding.textInputLayoutPassword.error = "올바른 입력 값이 아닙니다!"
//                else{
//
//                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
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
                Toast.makeText(this, "Failed Google Login", Toast.LENGTH_SHORT).show()
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


}