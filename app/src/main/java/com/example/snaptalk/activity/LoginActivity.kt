package com.example.snaptalk.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import android.widget.Toast
import com.example.snaptalk.R
import com.example.snaptalk.activity.SignUpActivity
import com.example.snaptalk.activity.UserActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private  var auth: FirebaseAuth?=null
    private  var firebaseUser:FirebaseUser?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        if(firebaseUser!=null){
            val intent = Intent(this@LoginActivity, UserActivity::class.java)
            startActivity(intent)
            finish()
        }
        val btnLogin = this.findViewById<TextView>(R.id.btnLogin)
        val etEmail = this.findViewById<TextView>(R.id.etEmail)
        val etPassword = this.findViewById<TextView>(R.id.etPassword)

        btnLogin.setOnClickListener{
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password)){
                Toast.makeText(applicationContext, "email and password are required", Toast.LENGTH_SHORT).show()
            }
            else{
                auth?.signInWithEmailAndPassword(email, password)
                    ?.addOnCompleteListener(this){
                        if(it.isSuccessful){
                            etEmail.setText("")
                            etPassword.setText("")
                            val intent = Intent(this@LoginActivity, UserActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else{
                            Toast.makeText(applicationContext, "email or password invalid", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
        val btnSignUp = this.findViewById<TextView>(R.id.btnSignUp)
        btnSignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
            finish()
            }
        }
}