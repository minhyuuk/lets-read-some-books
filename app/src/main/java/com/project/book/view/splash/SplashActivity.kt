package com.project.book.view.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.constraintlayout.motion.widget.MotionLayout
import com.project.book.R
import com.project.book.base.BaseActivity
import com.project.book.databinding.ActivitySplashBinding
import com.project.book.view.login.LoginActivity
import com.project.book.view.main.MainActivity


class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        handler()
    }

    private fun handler(){
        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent)
        }, DURATION)
    }

    private fun nextActivity(){
        val motionLayout = findViewById<MotionLayout>(R.id.motionLayout)
        motionLayout.addTransitionListener(object :MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {

            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {

            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {

            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {

            }

        })
    }



    companion object {
        private const val DURATION : Long = 1600
    }

}
