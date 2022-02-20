package com.miso.chatapplication.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
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
    lateinit var btnSignout: Button
    lateinit var binding: ActivityMainBinding
    lateinit var firebaseDatabase: DatabaseReference
    lateinit var recycler_chatroom: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeView()
        initializeListener()
        setupRecycler()
    }

    fun initializeView() { //뷰 초기화
        try {
            firebaseDatabase = FirebaseDatabase.getInstance().getReference("ChatRoom")!!
            btnSignout = binding.btnSignout
            btnAddchatRoom = binding.btnNewMessage
            recycler_chatroom = binding.recyclerChatrooms
        }catch (e:Exception)
        {
            e.printStackTrace()
            Toast.makeText(this,"화면 초기화 중 오류가 발생하였습니다.",Toast.LENGTH_LONG).show()
        }
    }
    fun initializeListener()  //버튼 클릭 시 리스너 초기화
    {
        btnSignout.setOnClickListener()
        {
            signOut()
        }
        btnAddchatRoom.setOnClickListener()  //새 메시지 화면으로 이동
        {
            startActivity(Intent(this@MainActivity, AddChatRoomActivity::class.java))
            finish()
        }
    }

    fun setupRecycler() {
        recycler_chatroom.layoutManager = LinearLayoutManager(this)
        recycler_chatroom.adapter = RecyclerChatRoomsAdapter(this)
    }

    fun signOut()    //로그아웃 실행
    {
        try {
            val builder = AlertDialog.Builder(this)
                .setTitle("로그아웃")
                .setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("확인"
                ) { dialog, id ->
                    try {
                        FirebaseAuth.getInstance().signOut()             //로그아웃
                        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                        dialog.dismiss()
                        finish()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        dialog.dismiss()
                        Toast.makeText(this, "로그아웃 중 오류가 발생하였습니다.", Toast.LENGTH_LONG).show()
                    }
                }
                .setNegativeButton("취소"          //다이얼로그 닫기
                ) { dialog, id ->
                    dialog.dismiss()
                }
            builder.show()
        }catch (e:Exception)
        {
            e.printStackTrace()
            Toast.makeText(this,"로그아웃 중 오류가 발생하였습니다.",Toast.LENGTH_LONG).show()
        }
    }

    override fun onBackPressed() {
        signOut()
    }
}