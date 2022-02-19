package com.miso.chatapplication.chatroom

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.miso.chatapplication.addChatRoom.RecyclerUsersAdapter
import com.miso.chatapplication.main.MainActivity
import com.miso.chatapplication.databinding.ActivityChatroomBinding
import com.miso.chatapplication.model.ChatRoom
import com.miso.chatapplication.model.Message
import com.miso.chatapplication.model.User
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
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
    lateinit var chatRoomKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatroomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeView()
        setupRecycler()
    }

    fun initializeView() {
        chatRoom = (intent.getSerializableExtra("ChatRoom")) as ChatRoom
        chatRoomKey = intent.getStringExtra("ChatRoomKey")!!
        opponentUser = (intent.getSerializableExtra("Opponent")) as User
        Log.i("opponentUser", opponentUser.name.toString())
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
        txt_title.text = opponentUser!!.name ?: ""
        btn_submit.setOnClickListener()
        {
            putMessage()
        }
    }

    fun putMessage() {
        var myUid = FirebaseAuth.getInstance().currentUser!!.uid
        var localDateTime = LocalDateTime.now()
        var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        var dateTimeString = localDateTime.format(dateTimeFormatter).toString()
        var message = Message(myUid, dateTimeString, edt_message.text.toString())
        FirebaseDatabase.getInstance().getReference("ChatRoom").
        child("chatRooms").child(chatRoomKey).child("messages")
            .push().setValue(message).addOnSuccessListener {
            Log.i("putMessage", "메시지 전송에 성공하였습니다.")
            edt_message.text.clear()
        }
    }

    fun setupRecycler() {
        recycler_talks.layoutManager = LinearLayoutManager(this)
//        recycler_talks.adapter = RecyclerUsersAdapter(this)
    }
}