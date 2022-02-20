package com.miso.chatapplication.addChatRoom

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
import com.miso.chatapplication.main.MainActivity
import com.miso.chatapplication.R
import com.miso.chatapplication.chatroom.ChatRoomActivity
import com.miso.chatapplication.databinding.ListPersonItemBinding
import com.miso.chatapplication.model.ChatRoom
import com.miso.chatapplication.model.User

class RecyclerUsersAdapter(val context: Context) :
    RecyclerView.Adapter<RecyclerUsersAdapter.ViewHolder>() {
    var users: ArrayList<User> = arrayListOf()
    val allUsers: ArrayList<User> = arrayListOf()
    lateinit var currnentUser: User

    init {
        setupAllUserList()
    }

    fun setupAllUserList() {
        val myUid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        FirebaseDatabase.getInstance().getReference("User").child("users")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    users.clear()
                    for (data in snapshot.children) {
                        val item = data.getValue<User>()
                        if (item?.uid.equals(myUid)) {
                            currnentUser = item!!
                            continue
                        }
                        allUsers.add(item!!)
                    }
                    users = allUsers.clone() as ArrayList<User>
                    notifyDataSetChanged()
                }
            })
    }

    fun searchItem(target: String) {
        if (target.equals("")) {
            users = allUsers.clone() as ArrayList<User>
        } else {
            var matchedList = allUsers.filter { it.name!!.contains(target) }
            users.clear()
            matchedList.forEach { users.add(it) }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_person_item, parent, false)
        return ViewHolder(ListPersonItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txt_name.text = users[position].name
        holder.txt_email.text = users[position].email

        holder.background.setOnClickListener()
        {
            addChatRoom(position)
        }
    }

    fun addChatRoom(position: Int) {
        val opponent = users[position]
        var database = FirebaseDatabase.getInstance().getReference("ChatRoom")
        var chatRoom = ChatRoom(
            mapOf(currnentUser.uid!! to true, opponent.uid!! to true),
            null
        )
        var myUid = FirebaseAuth.getInstance().uid
        database.child("chatRooms")
            .orderByChild("users/${opponent.uid}").equalTo(true)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value == null) {
                        database.child("chatRooms").push().setValue(chatRoom).addOnSuccessListener {
                            goToChatRoom(chatRoom, opponent)
                        }
                    } else {
                        context.startActivity(Intent(context, MainActivity::class.java))
                        goToChatRoom(chatRoom, opponent)
                    }

                }
            })
    }

    fun goToChatRoom(chatRoom: ChatRoom, opponentUid: User) {
        var intent = Intent(context, ChatRoomActivity::class.java)
        intent.putExtra("ChatRoom", chatRoom)
        intent.putExtra("Opponent", opponentUid)
        intent.putExtra("ChatRoomKey", "")
        context.startActivity(intent)
        (context as AppCompatActivity).finish()
    }

    override fun getItemCount(): Int {
        return users.size
    }


    inner class ViewHolder(itemView: ListPersonItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var background = itemView.background
        var txt_name = itemView.txtName
        var txt_email = itemView.txtEmail
    }

}
