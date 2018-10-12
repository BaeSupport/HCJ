package com.globe.hcj.view.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.globe.hcj.MainActivity
import com.globe.hcj.R
import com.globe.hcj.constants.ROOM_ID
import com.globe.hcj.data.firestore.User
import com.globe.hcj.preference.TraySharedPreference
import com.globe.hcj.view.login.LoginAcitivity
import com.globe.hcj.view.pair.PairAddActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Created by baeminsu on 10/10/2018.
 */


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val db = FirebaseFirestore.getInstance()
        db.collection("user").document(FirebaseAuth.getInstance().currentUser!!.email!!).get()
                .addOnCompleteListener {

                }

        val email = TraySharedPreference(this).getString(ROOM_ID, "")




        val handler = Handler()
        handler.postDelayed({
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                val db = FirebaseFirestore.getInstance()
                var email = user.email
                val docRef = db.collection("user").document(email!!)
                docRef.get().addOnCompleteListener { p0 ->
                    if (p0.isSuccessful) {
                        val document = p0.result
                        if (document.exists()) {
                            val searchUser = document.toObject(User::class.java)
                            if (searchUser!!.pair.isBlank()) {
                                startActivity(Intent(this@SplashActivity, PairAddActivity::class.java))
                            } else {
                                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                            }
                        } else {

                            //TODO 회원가입은 되어 있는데 db에 정보가 없는 상황
                        }
                    }
                }
            } else {
                startActivity(Intent(this, LoginAcitivity::class.java))
                finish()
            }
        }, 1000)


    }
}