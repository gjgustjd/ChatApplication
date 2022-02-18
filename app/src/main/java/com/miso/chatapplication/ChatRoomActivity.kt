package com.miso.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.miso.chatapplication.databinding.ActivityChatroomBinding
import com.miso.chatapplication.main.MainActivity

class ChatRoomActivity : AppCompatActivity() {
    lateinit var btnBack:ImageButton
    lateinit var binding:ActivityChatroomBinding
    lateinit var recycler_messages: RecyclerView
    lateinit var firebaseDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatroomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeView()
    }

    fun initializeView()
    {
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("User")!!
        recycler_messages = binding.recyclerMessages
        btnBack = binding.imgbtnQuit
        btnBack.setOnClickListener()
        {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@ChatRoomActivity, MainActivity::class.java))
        finish()
    }

}