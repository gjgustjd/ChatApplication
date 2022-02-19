package com.miso.chatapplication.chatroom

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.miso.chatapplication.R
import com.miso.chatapplication.chatroom.ChatRoomActivity
import com.miso.chatapplication.databinding.ListChatroomItemBinding
import com.miso.chatapplication.databinding.ListTalkItemOthersBinding
import com.miso.chatapplication.model.ChatRoom
import com.miso.chatapplication.model.Message
import com.miso.chatapplication.model.User

class RecyclerMessagesAdapter(val context: Context, val chatRoomKey: String) :
    RecyclerView.Adapter<RecyclerMessagesAdapter.ViewHolder>() {
    var messages: ArrayList<Message> = arrayListOf()
    val myUid = FirebaseAuth.getInstance().currentUser?.uid.toString()

    init {
        setupAllUserList()
    }

    fun setupAllUserList() {

        FirebaseDatabase.getInstance().getReference("ChatRoom")
            .child("chatRooms").child(chatRoomKey).child("messages")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    messages.clear()
                    for (data in snapshot.children) {
                        messages.add(data.getValue<Message>()!!)
                    }
                    notifyDataSetChanged()
                }
            })

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.list_talk_item_others, parent, false)
        return ViewHolder(ListTalkItemOthersBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var message = messages[position]
        var sendDate = message.sended_date
        holder.txtMessage.text = message.content
        var dateText = ""
        var timeString = ""
        if (sendDate.isNotBlank()) {
            timeString = sendDate.substring(8, 12)
            var hour = timeString.substring(0, 2)
            var minute = timeString.substring(2, 4)

            var timeformat = "%02d:%02d"

            if (hour.toInt() > 11) {
                dateText += "오후 "
                dateText += timeformat.format(hour.toInt() - 12, minute.toInt())
            } else {
                dateText += "오전 "
                dateText += timeformat.format(hour.toInt(), minute.toInt())
            }

        }

        holder.txtDate.text = dateText
    }


    override fun getItemCount(): Int {
        return messages.size
    }


    inner class ViewHolder(itemView: ListTalkItemOthersBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var background = itemView.background
        var txtMessage = itemView.txtMessage
        var txtDate = itemView.txtDate
    }

}
