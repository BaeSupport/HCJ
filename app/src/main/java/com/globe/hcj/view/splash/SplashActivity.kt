package com.globe.hcj.view.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.globe.hcj.MainActivity
import com.globe.hcj.R
import com.globe.hcj.view.login.LoginAcitivity
import com.google.firebase.auth.FirebaseAuth

/**
 * Created by baeminsu on 10/10/2018.
 */


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val handler = Handler()
        handler.postDelayed(Runnable {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, LoginAcitivity::class.java))
                finish()
            }
        }, 1000)


    }
}