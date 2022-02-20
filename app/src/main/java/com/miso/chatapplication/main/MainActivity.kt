package com.miso.chatapplication.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.miso.chatapplication.LoginActivity
import com.miso.chatapplication.addChatRoom.AddChatRoomActivity
import com.miso.chatapplication.databinding.ActivityMainBinding
import com.miso.chatapplication.model.ChatRoom


@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {
    lateinit var btnAddchatRoom: Button
    lateinit var binding: ActivityMainBinding
    lateinit var firebaseDatabase: DatabaseReference
    lateinit var recycler_chatroom: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeView()
        setupRecycler()
    }

    fun initializeView() {
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("ChatRoom")!!
        btnAddchatRoom = binding.btnNewMessage
        btnAddchatRoom.setOnClickListener()
        {
            startActivity(Intent(this@MainActivity, AddChatRoomActivity::class.java))
            finish()
        }
        recycler_chatroom = binding.recyclerChatrooms
    }

    fun setupRecycler() {
        recycler_chatroom.layoutManager = LinearLayoutManager(this)
        recycler_chatroom.adapter = RecyclerChatRoomsAdapter(this)

    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
            .setTitle("로그아웃")
            .setMessage("로그아웃 하시겠습니까?")
            .setPositiveButton("확인",
                { dialog, id ->
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    dialog.dismiss()
                    finish()
                })
            .setNegativeButton("취소",
                {dialog,id->
                    dialog.dismiss()
                })
        builder.show()
    }
}