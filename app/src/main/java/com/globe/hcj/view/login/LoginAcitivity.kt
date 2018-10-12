package com.globe.hcj.view.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.globe.hcj.MainActivity
import com.globe.hcj.R
import com.globe.hcj.data.firestore.User
import com.globe.hcj.view.pair.PairAddActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activitiy_login.*


/**
 * Created by baeminsu on 10/10/2018.
 */

class LoginAcitivity : AppCompatActivity() {

    val mAuth = FirebaseAuth.getInstance()
    val RC_SIGN_IN = 1000;
    private var mGoogleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activitiy_login);

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInBtn.setOnClickListener {
            signIn()
        }
    }


    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {

        Log.e("체크11", "체크")

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    Log.e("체크22", "체크")
                    if (task.isSuccessful) {
                        val user = FirebaseAuth.getInstance().currentUser;
                        val db = FirebaseFirestore.getInstance()
                        val email = user!!.email
                        val userData = User(user.displayName!!, user.email!!, "", "")
                        val docRef = db.collection("user").document(email!!)
                        docRef.get().addOnCompleteListener { p0 ->
                            if (p0.isSuccessful) {
                                val document = p0.result
                                if (!document.exists()) {
                                    Log.e("체크22-22", "추가과정")
                                    db.collection("user").document(email).set(userData)
                                            .addOnCompleteListener {
                                                if (it.isSuccessful) {
                                                    startActivity(Intent(this@LoginAcitivity, PairAddActivity::class.java))
                                                    finish()
                                                }
                                            }
                                } else {
                                    //로그인했는데 이미 디비에 정보가 있는 회원 케이스
                                    val user = document.toObject(User::class.java)
                                    if (user!!.pair.isBlank()) {
                                        startActivity(Intent(this@LoginAcitivity, PairAddActivity::class.java))
                                    } else {
                                        startActivity(Intent(this@LoginAcitivity, MainActivity::class.java))
                                    }
                                }
                            }
                        }
                    } else {
                        //회원가입 실패
                    }

                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Log.e("체크", "API에러")
                Log.e("체크", e.toString())
            }

        }


    }
}