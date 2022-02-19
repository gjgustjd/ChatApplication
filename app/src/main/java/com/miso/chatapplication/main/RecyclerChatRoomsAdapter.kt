package com.miso.chatapplication.main

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
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
import com.miso.chatapplication.model.ChatRoom
import com.miso.chatapplication.model.User
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.O)
class RecyclerChatRoomsAdapter(val context: Context) :
    RecyclerView.Adapter<RecyclerChatRoomsAdapter.ViewHolder>() {
    var chatRooms: ArrayList<ChatRoom> = arrayListOf()
    var chatRoomKeys: ArrayList<String> = arrayListOf()
    val myUid = FirebaseAuth.getInstance().currentUser?.uid.toString()

    init {
        setupAllUserList()
    }

    fun setupAllUserList() {

        FirebaseDatabase.getInstance().getReference("ChatRoom").child("chatRooms")
            .orderByChild("users/$myUid").equalTo(true)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    chatRooms.clear()
                    for (data in snapshot.children) {
                        chatRooms.add(data.getValue<ChatRoom>()!!)
                        chatRoomKeys.add(data.key!!)
                    }
                    notifyDataSetChanged()
                }
            })

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_chatroom_item, parent, false)
        return ViewHolder(ListChatroomItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var userIdList = chatRooms[position].users!!.keys
        var opponent = userIdList.first { !it.equals(myUid) }
        FirebaseDatabase.getInstance().getReference("User").child("users").orderByChild("uid")
            .equalTo(opponent)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        holder.chatRoomKey = data.key.toString()!!
                        holder.opponentUser = data.getValue<User>()!!
                        holder.txt_name.text = data.getValue<User>()!!.name.toString()
                    }
                }
            })
        holder.background.setOnClickListener()
        {
            var intent = Intent(context, ChatRoomActivity::class.java)
            intent.putExtra("ChatRoom", chatRooms.get(position))
            intent.putExtra("Opponent", holder.opponentUser)
            intent.putExtra("ChatRoomKey", chatRoomKeys[position])
            context.startActivity(intent)
            (context as AppCompatActivity).finish()
        }

        if (chatRooms[position].messages!!.size > 0) {
            holder.txt_chatCount.visibility = View.VISIBLE
            holder.txt_chatCount.text = chatRooms[position].messages!!.size.toString()
            var lastMessage =
                chatRooms[position].messages!!.values.sortedWith(compareBy({ it.sended_date }))
                    .last()
            holder.txt_message.text = lastMessage.content
            holder.txt_date.text = getLastMessageTimeString(lastMessage.sended_date)
        } else
            holder.txt_chatCount.visibility = View.GONE

    }

    fun getLastMessageTimeString(lastTimeString: String): String {
        var currentTime = LocalDateTime.now().atZone(TimeZone.getDefault().toZoneId())
        var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")

        var messageMonth = lastTimeString.substring(4, 6).toInt()
        var messageDate = lastTimeString.substring(6, 8).toInt()
        var messageHour = lastTimeString.substring(8, 10).toInt()
        var messageMinute = lastTimeString.substring(10, 12).toInt()

        var formattedCurrentTimeString = currentTime.format(dateTimeFormatter)
        var currentMonth = formattedCurrentTimeString.substring(4, 6).toInt()
        var currentDate = formattedCurrentTimeString.substring(6, 8).toInt()
        var currentHour = formattedCurrentTimeString.substring(8, 10).toInt()
        var currentMinute = formattedCurrentTimeString.substring(10, 12).toInt()

        var monthAgo = currentMonth - messageMonth
        var dayAgo = currentDate - messageDate
        var hourAgo = currentHour - messageHour
        var minuteAgo = currentMinute - messageMinute

        if (monthAgo > 0)
            return monthAgo.toString() + "개월 전"
        else {
            if (dayAgo > 0) {
                if (dayAgo == 1)
                    return "어제"
                else
                    return dayAgo.toString()+"일 전"
            } else {
                if(hourAgo>0)
                    return hourAgo.toString()+"시간 전"
                else
                {
                    if(minuteAgo>0)
                        return minuteAgo.toString()+"분 전"
                    else
                        return "방금"
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return chatRooms.size
    }

    inner class ViewHolder(itemView: ListChatroomItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var opponentUser = User("", "")
        var chatRoomKey = ""
        var background = itemView.background
        var txt_name = itemView.txtName
        var txt_message = itemView.txtMessage
        var txt_date = itemView.txtMessageDate
        var txt_chatCount = itemView.txtChatCount
    }

}
