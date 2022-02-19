package com.miso.chatapplication.chatroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.miso.chatapplication.addChatRoom.RecyclerUsersAdapter
import com.miso.chatapplication.main.MainActivity
import com.miso.chatapplication.databinding.ActivityChatroomBinding
import com.miso.chatapplication.model.ChatRoom
import com.miso.chatapplication.model.User

class ChatRoomActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatroomBinding
    lateinit var btn_exit: ImageButton
    lateinit var btn_submit: Button
    lateinit var txt_title: TextView
    lateinit var edt_message: EditText
    lateinit var firebaseDatabase: DatabaseReference
    lateinit var recycler_talks: RecyclerView
    lateinit var chatRoom: ChatRoom
    lateinit var opponentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatroomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeView()
        setupRecycler()
    }

    fun initializeView() {
        chatRoom = (intent.getSerializableExtra("ChatRoom")) as ChatRoom
        opponentUser = (intent.getSerializableExtra("Opponent")) as User
        Log.i("opponentUser",opponentUser.name.toString())
        var myUid = FirebaseAuth.getInstance().currentUser?.uid
        firebaseDatabase = FirebaseDatabase.getInstance().reference!!
        btn_exit = binding.imgbtnQuit
        btn_exit.setOnClickListener()
        {
            startActivity(Intent(this@ChatRoomActivity, MainActivity::class.java))
        }
        edt_message = binding.edtMessage
        recycler_talks = binding.recyclerMessages
        btn_submit = binding.btnSubmit
        txt_title = binding.txtTItle
        txt_title.text = opponentUser!!.name?:""

    }

    fun setupRecycler() {
        recycler_talks.layoutManager = LinearLayoutManager(this)
//        recycler_talks.adapter = RecyclerUsersAdapter(this)
    }
}