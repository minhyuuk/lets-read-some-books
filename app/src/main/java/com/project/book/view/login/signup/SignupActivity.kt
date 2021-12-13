package com.project.book.view.login.signup

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.project.book.R
import com.project.book.base.BaseActivity
import com.project.book.databinding.ActivitySignupBinding
import com.project.book.util.Extensions.toast
import com.project.book.view.login.LoginActivity
import kotlin.math.log

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

            try {
                createUser(email, password)
            }catch (e : FirebaseAuthUserCollisionException){
                Log.d("SignUp",e.toString())
            }
        }
    }

    private fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful && (binding.editPassword.text.toString() == binding.editPasswordCheck.text.toString())) {
                    toast("회원가입 성공!")
                    Log.d(TAG,"signInSuccess")
                    var intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0,0)
                    finish()
                } else if (!task.exception?.message.isNullOrEmpty() && (binding.editPassword.text.length <= 5)){
                    Log.d(TAG,task.exception?.message.toString())
                    dialog(binding.root)
                }else if(!task.exception?.message.isNullOrEmpty()){
                    Log.d(TAG,task.exception?.message.toString())
                    toast("이미 있는 이메일입니다. 다른 이메일로 가입하세요.")
                }
                else {
                    Log.d(TAG,task.result.toString())
                    dialog(binding.root)
                }
            }
            .addOnFailureListener {
//                toast("이미 존재하는 이메일입니다.")
            }
    }
    private fun dialog(view: View) {
        AlertDialog.Builder(view.context).apply {
            setTitle("비밀번호 6자 이상")
            setMessage("비밀번호는 6자 이상으로 설정해주세요!")
            setPositiveButton("네 알겠어요!", DialogInterface.OnClickListener { dialog, which ->
            })
            show()
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
                if (binding.editPassword.text.toString() == binding.editPasswordCheck.text.toString())
                { binding.textFailToPassword.text = "" }
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


    companion object{
        private val TAG = "SignUpActivity"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}