package com.globe.hcj.view.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.globe.hcj.MainActivity
import com.globe.hcj.R
import com.globe.hcj.constants.ROOM_ID
import com.globe.hcj.data.firestore.User
import com.globe.hcj.data.local.AreaItem
import com.globe.hcj.data.local.inputStreamToString
import com.globe.hcj.preference.TraySharedPreference
import com.globe.hcj.view.login.LoginAcitivity
import com.globe.hcj.view.pair.PairAddActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by baeminsu on 10/10/2018.
 */


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val postData: String? = inputStreamToString(resources.openRawResource(R.raw.post_data))

        val area: ArrayList<AreaItem> = Gson().fromJson(postData, object : TypeToken<List<AreaItem>>() {}.type)

        Log.e("asdf", area[0].area)
        Log.e("asdf", area[0].list?.toString())


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
                                val roomId = TraySharedPreference(this).getString(ROOM_ID, "")
                                if (roomId.isNullOrEmpty())
                                    TraySharedPreference(this).put(ROOM_ID, searchUser.roomId)
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