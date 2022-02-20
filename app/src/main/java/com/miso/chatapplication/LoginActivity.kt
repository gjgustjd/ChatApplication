package com.miso.chatapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.miso.chatapplication.databinding.ActivityLoginBinding
import com.miso.chatapplication.main.MainActivity


class LoginActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var btn_signUp: Button
    lateinit var btn_signIn: Button
    lateinit var edt_email: EditText
    lateinit var edt_password: EditText
    lateinit var binding: ActivityLoginBinding
    lateinit var preference: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initProperty()
        initializeView()
        initializeListener()
    }

    fun initProperty() {
        auth = FirebaseAuth.getInstance()
        preference = getSharedPreferences("setting", MODE_PRIVATE)
    }

    fun initializeView() {
        btn_signUp = binding.btnSignup
        btn_signIn = binding.btnSignIn
        edt_email = binding.edtEmail
        edt_password = binding.edtPassword

        edt_email.setText(preference.getString("email", ""))
        edt_password.setText(preference.getString("password", ""))
    }

    fun initializeListener() {
        btn_signIn.setOnClickListener()
        {
            signInWithEmailAndPassword()
        }
        btn_signUp.setOnClickListener()
        {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    fun signInWithEmailAndPassword() {
        if (edt_email.text.toString().isNullOrBlank() &&
            edt_password.text.toString().isNullOrBlank())
            Toast.makeText(this, "아이디 또는 패스워드를 입력해주세요", Toast.LENGTH_SHORT).show()
        else {
            auth.signInWithEmailAndPassword(edt_email.text.toString(), edt_password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("로그인", "성공")
                        val user = auth.currentUser
                        updateUI(user)
                        finish()
                    } else {
                        Toast.makeText(this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun updateUI(user: FirebaseUser?) { //update ui code here
        if (user != null) {
            try {
                var preference = getSharedPreferences("setting", MODE_PRIVATE).edit()
                preference.putString("email", edt_email.text.toString())
                preference.putString("password", edt_password.text.toString())
                preference.apply()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}