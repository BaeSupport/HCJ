package com.globe.hcj.view.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.globe.hcj.R
import com.globe.hcj.data.firestore.IMessageInterface
import com.globe.hcj.data.firestore.TextMessage
import com.globe.hcj.view.main.apdater.ChatRecyclerViewAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_chat.view.*


/**
 * Created by baeminsu on 10/10/2018.
 */

class ChatFragment() : Fragment() {


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {

        if (isVisibleToUser) {

        } else {

        }

    }

    private var messageListener: ListenerRegistration? = null
    private val adapter = ChatRecyclerViewAdapter(context)
    private lateinit var recyclerview: RecyclerView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        with(view) {

            val layoutManager = LinearLayoutManager(context)
            layoutManager.stackFromEnd = true
            view.chat_room_recycler.layoutManager = layoutManager
            view.chat_room_recycler.adapter = adapter
            recyclerview = view.chat_room_recycler
            setView(view)
        }



        return view
    }

    private fun setView(view: View) {

        view.chat_room_edit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                view.chat_room_send.isEnabled = !view.chat_room_edit.getText().toString().isEmpty()

            }
        })

    }

    fun addMessageListener(roomId: String) {
        val db = FirebaseFirestore.getInstance()
        var allChatMessageRef = db.collection("room").document(roomId).collection("message")
        messageListener = allChatMessageRef.addSnapshotListener { documentSnapshots, e ->
            if (documentSnapshots != null) {
                for (dc in documentSnapshots.documentChanges) {
                    var message: IMessageInterface? = null
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            //TODO 메세지 타입 여러개일시 타입 분류
                            message = dc.document.toObject(TextMessage::class.java)
                            db.runTransaction {
                                if (it.get(dc.document.reference).get("email") != FirebaseAuth.getInstance().currentUser!!.email &&
                                        (it.get(dc.document.reference).get("unReadCount") == 1)) {
                                    it.update(dc.document.reference, "unReadCount", 0)
                                }
                            }
                            adapter.addMessage(message)
                            recyclerview.smoothScrollToPosition(adapter.itemCount)

                        }
                        DocumentChange.Type.MODIFIED -> {
                            //TODO 메세지 타입 여러개일시 타입 분류
                            message = dc.document.toObject(TextMessage::class.java)
                            adapter.updateMessage(message)
                        }
                        DocumentChange.Type.REMOVED -> {
                        }


                    }
                }


            }
        }


    }

}