package com.miso.chatapplication.main

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
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
import com.miso.chatapplication.model.ChatRoom
import com.miso.chatapplication.model.User

class RecyclerChatRoomsAdapter(val context: Context) :
    RecyclerView.Adapter<RecyclerChatRoomsAdapter.ViewHolder>() {
    var chatRooms: ArrayList<ChatRoom> = arrayListOf()
    var chatRoomKeys:ArrayList<String> = arrayListOf()
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
        var opponent = userIdList.first{!it.equals(myUid)}
        FirebaseDatabase.getInstance().getReference("User").child("users").orderByChild("uid").equalTo(opponent)
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
            var intent = Intent(context,ChatRoomActivity::class.java)
            intent.putExtra("ChatRoom",chatRooms.get(position))
            intent.putExtra("Opponent",holder.opponentUser)
            intent.putExtra("ChatRoomKey",chatRoomKeys[position])
           context.startActivity(intent)
            (context as AppCompatActivity).finish()
        }
        if(chatRooms[position].messages!!.size>0)
        {
            holder.txt_chatCount.visibility = View.VISIBLE
            holder.txt_chatCount.text = chatRooms[position].messages!!.size.toString()
        }
        else
           holder.txt_chatCount.visibility = View.GONE
    }


    override fun getItemCount(): Int {
        return chatRooms.size
    }


    inner class ViewHolder(itemView: ListChatroomItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var opponentUser= User("","")
        var chatRoomKey = ""
        var background = itemView.background
        var txt_name = itemView.txtName
        var txt_message = itemView.txtMessage
        var txt_date = itemView.txtMessageDate
        var txt_chatCount = itemView.txtChatCount
    }

}
