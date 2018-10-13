package com.globe.hcj.view.main.apdater

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.globe.hcj.R
import com.globe.hcj.data.firestore.IMessageInterface
import com.globe.hcj.data.firestore.TextMessage
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.recycler_item_recive_message.view.*
import kotlinx.android.synthetic.main.recycler_item_sent_message.view.*
import java.text.SimpleDateFormat


class ChatRecyclerViewAdapter(var context: Context) : RecyclerView.Adapter<ChatMessage>() {
    val SEND_MESSAGE = 10
    val RECEIVE_MESSAGE = 20
    var list = ArrayList<IMessageInterface>()

    override fun getItemViewType(position: Int): Int {
        //TODO 사진 전송시 수신,발신 별로 각각 텍스트 포토 로 4개로 구분
        val info = list[position] as TextMessage
        if (info.sendEmail == FirebaseAuth.getInstance().currentUser!!.email) {
            return SEND_MESSAGE
        } else {
            return RECEIVE_MESSAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ChatMessage {
        var view: View? = null
        return if (viewType == SEND_MESSAGE) {
            view = LayoutInflater.from(context).inflate(R.layout.recycler_item_sent_message, parent, false)
            MyTextMessageViewHolder(view)
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.recycler_item_recive_message, parent, false)
            PairTextMessageViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ChatMessage, position: Int) {
        holder.setView(list[position])
    }

    fun addMessage(message: IMessageInterface) {
        list.add(message)
    }

    fun updateMessage(message: IMessageInterface) {
        //TODO Message 종류 의존성
        val position = getItemPosition((message as TextMessage).messageId)
        list[position] = message
    }

    private fun getItemPosition(messageId: String): Int {
        var position = 0
        for (message in list) {
            if (message is TextMessage && message.messageId == messageId) {
                return position
            }
            position++
        }
        return -1
    }

}
abstract class ChatMessage(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun setView(data: IMessageInterface)
}


class MyTextMessageViewHolder(val view: View) : ChatMessage(view) {
    private val dateFormat = SimpleDateFormat("hh:mm")

    override fun setView(data: IMessageInterface) {
        val convertData = data as TextMessage
        view.send_text_message_body.text = convertData.message
        view.send_text_message_time.text = dateFormat.format(convertData.messageDate)
        if (convertData.unReadCount == 0) {
            view.send_text_message_unread_count.visibility = View.INVISIBLE
        } else {
            view.send_text_message_unread_count.visibility = View.VISIBLE
        }
    }
}

class PairTextMessageViewHolder(val view: View) : ChatMessage(view) {
    private val dateFormat = SimpleDateFormat("hh:mm")
    override fun setView(data: IMessageInterface) {
        val convertData = data as TextMessage
        view.receive_text_message_name.text = data.sendName
        view.receive_text_message_body.text = convertData.message
        view.receive_text_message_time.text = dateFormat.format(convertData.messageDate)
        if (convertData.unReadCount == 0) {
            view.receive_text_message_unread_count.visibility = View.INVISIBLE
        } else {
            view.receive_text_message_unread_count.visibility = View.VISIBLE
        }
    }
}