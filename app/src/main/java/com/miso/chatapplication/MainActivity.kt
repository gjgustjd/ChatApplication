package com.miso.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.miso.chatapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var btnAddchatRoom:Button
    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeView()
    }
    fun initializeView()
    {
        btnAddchatRoom = binding.btnNewMessage
        btnAddchatRoom.setOnClickListener()
        {
           startActivity(Intent(this@MainActivity,AddChatrRoomActivity::class.java))
            finish()
        }
    }
}