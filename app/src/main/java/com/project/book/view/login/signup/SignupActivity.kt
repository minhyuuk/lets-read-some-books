package com.project.book.view.login.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.google.firebase.auth.FirebaseAuth
import com.project.book.R
import com.project.book.base.BaseActivity
import com.project.book.databinding.ActivitySignupBinding
import com.project.book.util.Extensions.toast
import com.project.book.view.login.LoginActivity

class SignupActivity : BaseActivity<ActivitySignupBinding>(R.layout.activity_signup) {

    private var _binding: ActivitySignupBinding? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        signUp()
        equalPassword()
        backPage()
    }

    private fun signUp() {
        binding.signupButton.setOnClickListener {
            val email = binding.editEmail.text.toString().trim()
            val password = binding.editPassword.text.toString().trim()

            createUser(email, password)
        }
    }

    private fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    toast("회원가입 성공")
                    var intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    toast("회원가입 실패")
                }
            }
            .addOnFailureListener {
                toast("회원가입 실패")
            }
    }

    private fun equalPassword() {
        binding.editPasswordCheck.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // 텍스트 입력 후
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // 텍스트에 변화가 있는 경우
            }

            override fun afterTextChanged(p0: Editable?) {
                if (binding.editPassword.text.toString() == binding.editPasswordCheck.text.toString()) { binding.textFailToPassword.text = "" }
                else { binding.textFailToPassword.text = "비밀번호가 일치하지 않습니다" }

            }

        })
    }
    private fun backPage(){
        binding.backPressButton.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
            overridePendingTransition(0,0)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}