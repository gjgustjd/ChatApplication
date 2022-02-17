package com.miso.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.miso.chatapplication.addChatRoom.AddChatRoomActivity
import com.miso.chatapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var btnAddchatRoom:Button
    lateinit var binding:ActivityMainBinding
    lateinit var firebaseDatabase: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeView()
        getUserList()
    }
    fun initializeView()
    {
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("User")!!
        btnAddchatRoom = binding.btnNewMessage
        btnAddchatRoom.setOnClickListener()
        {
           startActivity(Intent(this@MainActivity, AddChatRoomActivity::class.java))
            finish()
        }
    }
    fun getUserList()
    {
        firebaseDatabase.child("users").get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }
}