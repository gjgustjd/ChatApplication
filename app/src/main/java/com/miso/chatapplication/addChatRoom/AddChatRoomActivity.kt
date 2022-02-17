package com.miso.chatapplication.addChatRoom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.miso.chatapplication.MainActivity
import com.miso.chatapplication.databinding.ActivityAddChatroomBinding

class AddChatRoomActivity : AppCompatActivity() {
    lateinit var binding:ActivityAddChatroomBinding
    lateinit var btn_exit: ImageButton
    lateinit var edt_opponent:EditText
    lateinit var firebaseDatabase:DatabaseReference
    lateinit var recycler_people:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddChatroomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeView()
        getUserList()
        setupRecycler()
    }

    fun initializeView()
    {
        firebaseDatabase = FirebaseDatabase.getInstance().reference!!
        btn_exit = binding.imgbtnBack
        btn_exit.setOnClickListener()
        {
            startActivity(Intent(this@AddChatRoomActivity, MainActivity::class.java))
        }
        edt_opponent = binding.edtOpponentName
        recycler_people = binding.recyclerPeoples

    }
    fun getUserList()
    {
//        firebaseDatabase.child("users").get().addOnSuccessListener {
//            Log.i("firebase", "Got value ${it.value}")
//        }.addOnFailureListener{
//            Log.e("firebase", "Error getting data", it)
//        }
    }
    fun setupRecycler()
    {
       recycler_people.layoutManager = LinearLayoutManager(this)
        recycler_people.adapter = RecyclerUsersAdapter(this)
    }
}