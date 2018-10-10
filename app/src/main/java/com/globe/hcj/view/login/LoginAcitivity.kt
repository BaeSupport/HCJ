package com.globe.hcj.view.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.globe.hcj.MainActivity
import com.globe.hcj.R
import com.globe.hcj.data.firestore.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
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

        Log.e("체크", "체크11")

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = FirebaseAuth.getInstance().currentUser;
                        var db = FirebaseFirestore.getInstance()
                        val email = user!!.email
                        var userData = User(user.displayName!!, user.email!!, "", "")
                        db.collection("user").document(email!!).set(userData)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        startActivity(Intent(this, MainActivity::class.java))
                                        finish()
                                    } else {
                                        Log.e("체크", it.exception.toString())
                                        Toast.makeText(this, "회원가입 오류 100, 관리자에게 문의해주세요.", Toast.LENGTH_SHORT).show()
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