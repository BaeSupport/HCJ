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
import android.widget.EditText
import com.globe.hcj.R
import com.globe.hcj.constants.ROOM_ID
import com.globe.hcj.data.firestore.IMessageInterface
import com.globe.hcj.data.firestore.TextMessage
import com.globe.hcj.preference.TraySharedPreference
import com.globe.hcj.view.main.apdater.ChatRecyclerViewAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_chat.view.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by baeminsu on 10/10/2018.
 */

class ChatFragment : Fragment() {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    private lateinit var roomId: String
    lateinit var messageEdit: EditText

    private var messageListener: ListenerRegistration? = null
    private lateinit var adapter: ChatRecyclerViewAdapter
    private lateinit var recyclerview: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        roomId = TraySharedPreference(context!!).getString(ROOM_ID, "")!!
        adapter = ChatRecyclerViewAdapter(context!!)

        with(view) {
            val layoutManager = LinearLayoutManager(context)
            layoutManager.stackFromEnd = true
            view.chat_room_recycler.layoutManager = layoutManager
            view.chat_room_recycler.adapter = adapter
            recyclerview = view.chat_room_recycler
            setView(view)
        }
        addMessageListener()
        return view
    }

    private fun setView(view: View) {
        messageEdit = view.chat_room_edit
        view.chat_room_edit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                view.chat_room_send.isEnabled = !view.chat_room_edit.getText().toString().isEmpty()

            }
        })

        view.chat_room_send.setOnClickListener {
            sendMessage()
        }

    }

    private fun addMessageListener() {

        val db = FirebaseFirestore.getInstance()
        val allChatMessageRef = db.collection("room").document(roomId).collection("message")
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
                            adapter.notifyDataSetChanged()
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

    private fun sendMessage() {

        val db = FirebaseFirestore.getInstance()
        val messageText = messageEdit.text.toString()
        val nowDate = Date()

        val newChatMessageRef = db.collection("room").document(roomId)
                .collection("message").document(dateFormat.format(nowDate))

        if (messageText.isEmpty())
            return

        val messageObject = TextMessage()
        with(messageObject) {
            sendName = FirebaseAuth.getInstance().currentUser!!.displayName!!
            message = messageText
            messageDate = nowDate
            unReadCount = 1
            messageId = newChatMessageRef.id
            sendEmail = FirebaseAuth.getInstance().currentUser!!.email!!
            profileURL = FirebaseAuth.getInstance().currentUser!!.photoUrl.toString()
        }
        messageEdit.setText("")

        newChatMessageRef.set(messageObject).addOnCompleteListener {


        }

    }
    
}