package com.miso.chatapplication.addChatRoom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.miso.chatapplication.main.MainActivity
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
        initializeListener()
        setupRecycler()
    }

    fun initializeView()   //뷰 초기화
    {
        firebaseDatabase = FirebaseDatabase.getInstance().reference!!
        btn_exit = binding.imgbtnBack
        edt_opponent = binding.edtOpponentName
        recycler_people = binding.recyclerPeoples
    }
    fun initializeListener()   //버튼 클릭 시 리스너 초기화
    {
        btn_exit.setOnClickListener()
        {
            startActivity(Intent(this@AddChatRoomActivity, MainActivity::class.java))
        }

        edt_opponent.addTextChangedListener(object :TextWatcher                  //검색 창에 입력된 글자가 변경될 때마다 검색 내용 업데이트
        {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                var adapter = recycler_people?.adapter as RecyclerUsersAdapter
                adapter.searchItem(s.toString())                  //입력된 검색어로 검색 진행 및 업데이트
            }
        })
    }
    fun setupRecycler()   //사용자 목록 초기화 및 업데이트
    {
       recycler_people.layoutManager = LinearLayoutManager(this)
        recycler_people.adapter = RecyclerUsersAdapter(this)
    }
}