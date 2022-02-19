package com.miso.chatapplication.chatroom

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.miso.chatapplication.R
import com.miso.chatapplication.databinding.ListTalkItemMineBinding
import com.miso.chatapplication.databinding.ListTalkItemOthersBinding
import com.miso.chatapplication.model.Message

class RecyclerMessagesAdapter(val context: Context, val chatRoomKey: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var messages: ArrayList<Message> = arrayListOf()
    val myUid = FirebaseAuth.getInstance().currentUser?.uid.toString()
    val recyclerView = (context as ChatRoomActivity).recycler_talks

    init {
        setupMessages()
    }

    fun setupMessages() {
        FirebaseDatabase.getInstance().getReference("ChatRoom")
            .child("chatRooms").child(chatRoomKey).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    messages.clear()
                    for (data in snapshot.children) {
                        messages.add(data.getValue<Message>()!!)
                    }
                    notifyDataSetChanged()
                    recyclerView.scrollToPosition(messages.size - 1)
                }
            })

    }


    override fun getItemViewType(position: Int): Int {
        return if (messages[position].senderUid.equals(myUid)) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        return when (viewType) {
            1 -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.list_talk_item_mine, parent, false)

                MyMessageViewHolder(ListTalkItemMineBinding.bind(view))
            }
            else -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.list_talk_item_others, parent, false)
                OtherMessageViewHolder(ListTalkItemOthersBinding.bind(view))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (messages[position].senderUid.equals(myUid)) {
            (holder as MyMessageViewHolder).bind(position)
        } else {
            (holder as OtherMessageViewHolder).bind(position)
        }
    }


    override fun getItemCount(): Int {
        return messages.size
    }


    inner class OtherMessageViewHolder(itemView: ListTalkItemOthersBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var background = itemView.background
        var txtMessage = itemView.txtMessage
        var txtDate = itemView.txtDate

        fun bind(position: Int) {
            var message = messages[position]
            var sendDate = message.sended_date
            txtMessage.text = message.content
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

            txtDate.text = dateText
        }
    }

    inner class MyMessageViewHolder(itemView: ListTalkItemMineBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var background = itemView.background
        var txtMessage = itemView.txtMessage
        var txtDate = itemView.txtDate

        fun bind(position: Int) {
            var message = messages[position]
            var sendDate = message.sended_date
            txtMessage.text = message.content
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

            txtDate.text = dateText
        }
    }

}
