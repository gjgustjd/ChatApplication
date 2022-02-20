package com.miso.chatapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.miso.chatapplication.databinding.ActivitySignupBinding
import com.miso.chatapplication.main.MainActivity
import com.miso.chatapplication.model.User


class SignUpActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var btn_signUp: Button
    lateinit var edt_email: EditText
    lateinit var edt_password: EditText
    lateinit var edt_name: EditText

    lateinit var binding: ActivitySignupBinding
    val SIGN_IN_CODE = 9001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeView()
        initializeListener()
    }

    fun initializeView() {
        auth = FirebaseAuth.getInstance()
        btn_signUp = binding.btnSignup
        edt_email = binding.edtEmail
        edt_password = binding.edtPassword
        edt_name = binding.edtOpponentName
    }

    fun initializeListener() {
        btn_signUp.setOnClickListener()
        {
            signUp()
        }
    }

    fun signUp() {
        var email = edt_email.text.toString()
        var password = edt_password.text.toString()
        var name = edt_name.text.toString()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userId = user?.uid
                    val userIdSt = userId.toString()
                    FirebaseDatabase.getInstance().getReference("User").child("users")
                        .child(userId.toString()).setValue(User(name, userIdSt, email))
                    Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    Log.e("UserId", "$userId")
                    startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                } else
                    Toast.makeText(this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show()

            }

    }
}