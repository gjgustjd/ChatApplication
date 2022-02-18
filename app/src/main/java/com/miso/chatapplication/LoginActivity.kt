package com.miso.chatapplication

import android.content.Context
import android.content.Intent
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
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var btn_googleSiginIn: SignInButton
    lateinit var btn_signUp: Button
    lateinit var btn_signIn: Button
    lateinit var edt_email: EditText
    lateinit var edt_password: EditText
    lateinit var binding: ActivityLoginBinding
    val SIGN_IN_CODE = 9001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeView()
        initializeListener()
    }

    fun initializeView() {
        btn_googleSiginIn = binding.btnLoginGoogle
        btn_signUp = binding.btnSignup
        btn_signIn = binding.btnSignIn
        edt_email = binding.edtEmail
        edt_password = binding.edtPassword

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("218608698906-u8i9gc76k8cc7tj0jgf1a8ud4888elfb.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        var preference = getSharedPreferences("setting", MODE_PRIVATE)
        edt_email.setText(preference.getString("email", ""))
        edt_password.setText(preference.getString("password", ""))
    }

    fun initializeListener() {
        btn_googleSiginIn.setOnClickListener()
        {
            signIn()
        }
        btn_signIn.setOnClickListener()
        {
            signInWithEmailAndPassword()
        }
        btn_signUp.setOnClickListener()
        {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    fun signIn() {
        val signInIntent: Intent = googleSignInClient.getSignInIntent()
        startActivityForResult(signInIntent, SIGN_IN_CODE)
    }

    fun signInWithEmailAndPassword() {
        if (edt_email.text.toString().isNullOrBlank() && edt_password.text.toString()
                .isNullOrBlank()
        )
            Toast.makeText(this, "아이디 또는 패스워드를 입력해주세요", Toast.LENGTH_SHORT).show()

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                e.printStackTrace()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        val user: FirebaseUser = auth.currentUser!!
                        updateUI(user)
                    } else {
                        Toast.makeText(this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                })
    }

    private fun updateUI(user: FirebaseUser?) { //update ui code here
        if (user != null) {
            var preference = getSharedPreferences("setting", MODE_PRIVATE).edit()
            preference.putString("email", edt_email.text.toString())
            preference.putString("password", edt_password.text.toString())
            preference.apply()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}