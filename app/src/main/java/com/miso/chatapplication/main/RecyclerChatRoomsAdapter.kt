package com.miso.chatapplication.main

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.miso.chatapplication.main.MainActivity
import com.miso.chatapplication.R
import com.miso.chatapplication.databinding.ListPersonItemBinding
import com.miso.chatapplication.model.ChatRoom
import com.miso.chatapplication.model.User

class RecyclerChatRoomsAdapter(val context: Context) :
    RecyclerView.Adapter<RecyclerChatRoomsAdapter.ViewHolder>() {
    var chatRooms: ArrayList<ChatRoom> = arrayListOf()

    init {
        setupAllUserList()
    }

    fun setupAllUserList() {
        val myUid = FirebaseAuth.getInstance().currentUser?.uid.toString()

        FirebaseDatabase.getInstance().getReference("ChatRoom").child("chatRooms")
            .orderByChild("users/$myUid").equalTo(true)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    chatRooms.clear()
                    for (data in snapshot.children) {
                        chatRooms.add(data.getValue<ChatRoom>()!!)
                    }
                    notifyDataSetChanged()
                }
            })

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_person_item, parent, false)
        return ViewHolder(ListPersonItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }


    override fun getItemCount(): Int {
        return chatRooms.size
    }


    inner class ViewHolder(itemView: ListPersonItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var background = itemView.background
        var txt_name = itemView.txtName
        var txt_email = itemView.txtEmail
    }

}
