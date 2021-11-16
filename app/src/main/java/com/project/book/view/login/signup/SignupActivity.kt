package com.project.book.view.login.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.project.book.databinding.ActivitySignupBinding
import com.project.book.view.login.LoginActivity

class SignupActivity : AppCompatActivity() {

    private var _binding : ActivitySignupBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getUserInfo()
        passwordCorrect()
    }

    private fun getUserInfo(){
        auth = FirebaseAuth.getInstance()
        binding.signupButton.setOnClickListener{
            val email = binding.editEmail.text.toString().trim()
            val password = binding.editPassword.text.toString().trim()

            createUser(email = email, password = password)
        }
    }

    private fun createUser(email: String,password : String){

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if (it.isSuccessful && password == binding.editPasswordCheck.toString()){
                    Toast.makeText(this,"회원가입 성공했습니다!",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,LoginActivity::class.java)
                    startActivity(intent)

                    val user = auth.currentUser

                }else{
                    Toast.makeText(this,"회원가입에 실패했습니다",Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun passwordCorrect(){
        binding.editPasswordCheck.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if(binding.editPassword.text.toString() == binding.editPasswordCheck.text.toString()){
                    binding.textFailToPassword.text = ""
                }else{
                    binding.textFailToPassword.text = "비밀번호가 일치하지 않습니다"
                }
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}