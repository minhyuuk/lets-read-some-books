package com.project.book.view.splash


import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import com.project.book.R
import com.project.book.base.BaseActivity
import com.project.book.databinding.ActivitySplashBinding
import com.project.book.util.Extensions.toast
import com.project.book.view.login.LoginActivity
import com.project.book.view.main.MainActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 현재 기기의 SDK버전이 안드로이드11 보다 크거나 같다면
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        if(isConnectInternet() != "null"){ // 인터넷 연결 성공
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                val intent = Intent(baseContext, LoginActivity::class.java)
                startActivity(intent)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                finish()
            }, DURATION)
        }
        else{ // 인터넷 연결 실패

        }
    }

    private fun isConnectInternet(): String { // 인터넷 연결 체크 함수
        val cm: ConnectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = cm.activeNetworkInfo
        return networkInfo.toString()
    }

    companion object {
        private const val DURATION: Long = 1600
    }

}
