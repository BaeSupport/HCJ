package com.globe.hcj.view.pair

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.globe.hcj.MainActivity
import com.globe.hcj.R
import com.globe.hcj.data.firestore.Room
import com.globe.hcj.data.firestore.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_pair_add.*
import com.google.firebase.firestore.DocumentReference


/**
 * Created by baeminsu on 11/10/2018.
 */
class PairAddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pair_add)

        val db = FirebaseFirestore.getInstance()

        pairAddBtn.setOnClickListener {
            val myEmail = FirebaseAuth.getInstance().currentUser!!.email!!
            val myUserRef = db.collection("user").document(myEmail)


            //짝 입력 대기중 상대방이 나를 입력했을때 체크
            myUserRef.get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val document = it.result
                    val user = document.toObject(User::class.java)
                    if (user!!.pair.isNotEmpty()) {
                        Toast.makeText(this@PairAddActivity, "다른분에 의해 짝이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@PairAddActivity, MainActivity::class.java))
                        finish()
                        return@addOnCompleteListener
                    }
                }

            }
            if (pairAddEt.text.toString().isNotEmpty()) {
                val pairEmail = pairAddEt.text.toString()
                //1. 상대방의 이메일이 있는 확인
                val pairEmailDoc = db.collection("user").document(pairEmail)
                pairEmailDoc.get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        val document = it.result
                        if (document.exists()) {
                            val pairUser = document.toObject(User::class.java)
                            //2. 상대방의 pair가 등록되어있는지 확인
                            if (pairUser!!.pair.isEmpty()) {
                                val newRoom = db.collection("room").document()
                                val roomId = newRoom.id
                                newRoom.set(Room(roomId))

                                val myUserRef = db.collection("user").document(myEmail)
                                val pairUserRef = db.collection("user").document(pairEmail)


                                //3. 나와 상대방에 pair 등록
                                myUserRef.update("pair", pairEmail, "roomId", roomId, "pairRefName", pairUserRef).addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        pairUserRef.update("pair", myEmail, "roomId", roomId, "pairRefName", myUserRef).addOnCompleteListener {
                                            if (it.isSuccessful) {
                                                //4. 완료 -> 메인페이지로 이동
                                                Toast.makeText(this@PairAddActivity, "등록이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                                                startActivity(Intent(this@PairAddActivity, MainActivity::class.java))
                                                finish()
                                            }
                                        }
                                    }
                                }
                            } else {
                                Toast.makeText(this@PairAddActivity, "이미 짝이 있는 회원이에요!", Toast.LENGTH_SHORT).show()
                            }

                        } else {
                            Toast.makeText(this@PairAddActivity, "존재하지 않는 회원입니다!", Toast.LENGTH_SHORT).show()
                        }

                    }

                }
            }
        }

    }
}
