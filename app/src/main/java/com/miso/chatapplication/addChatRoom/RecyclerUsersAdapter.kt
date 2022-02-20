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
    var users: ArrayList<User> = arrayListOf()        //검색어로 일치한 사용자 목록
    val allUsers: ArrayList<User> = arrayListOf()    //전체 사용자 목록
    lateinit var currnentUser: User

    init {
        setupAllUserList()
    }

    fun setupAllUserList() {        //전체 사용자 목록 불러오기
        val myUid = FirebaseAuth.getInstance().currentUser?.uid.toString()        //현재 사용자 아이디
        FirebaseDatabase.getInstance().getReference("User").child("users")   //사용자 데이터 요청
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    users.clear()
                    for (data in snapshot.children) {
                        val item = data.getValue<User>()
                        if (item?.uid.equals(myUid)) {
                            currnentUser = item!!             //전체 사용자 목록에서 현재 사용자는 제외
                            continue
                        }
                        allUsers.add(item!!)              //전체 사용자 목록에 추가
                    }
                    users = allUsers.clone() as ArrayList<User>
                    notifyDataSetChanged()              //화면 업데이트
                }
            })
    }

    fun searchItem(target: String) {            //검색
        if (target.equals("")) {      //검색어 없는 경우 전체 목록 표시
            users = allUsers.clone() as ArrayList<User>
        } else {
            var matchedList = allUsers.filter { it.name!!.contains(target) }         //검색어 포함된 항목 불러오기
            users.clear()
            matchedList.forEach { users.add(it) }
        }
        notifyDataSetChanged()          //화면 업데이트
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
            addChatRoom(position)        //해당 사용자 선택 시
        }
    }

    fun addChatRoom(position: Int) {     //채팅방 추가
        val opponent = users[position]   //채팅할 상대방 정보
        var database = FirebaseDatabase.getInstance().getReference("ChatRoom")    //넣을 database reference 세팅
        var chatRoom = ChatRoom(         //추가할 채팅방 정보 세팅
            mapOf(currnentUser.uid!! to true, opponent.uid!! to true),
            null
        )
        var myUid = FirebaseAuth.getInstance().uid       //내 Uid
        database.child("chatRooms")
            .orderByChild("users/${opponent.uid}").equalTo(true)       //상대방 Uid가 포함된 채팅방이 있는 지 확인
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value == null) {              //채팅방이 없는 경우
                        database.child("chatRooms").push().setValue(chatRoom).addOnSuccessListener {  // 채팅방 새로 생성 후 이동
                            goToChatRoom(chatRoom, opponent)
                        }
                    } else {
                        context.startActivity(Intent(context, MainActivity::class.java))
                        goToChatRoom(chatRoom, opponent)                    //해당 채팅방으로 이동
                    }

                }
            })
    }

    fun goToChatRoom(chatRoom: ChatRoom, opponentUid: User) {       //채팅방으로 이동
        var intent = Intent(context, ChatRoomActivity::class.java)
        intent.putExtra("ChatRoom", chatRoom)       //채팅방 정보
        intent.putExtra("Opponent", opponentUid)    //상대방 정보
        intent.putExtra("ChatRoomKey", "")   //채팅방 키
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
