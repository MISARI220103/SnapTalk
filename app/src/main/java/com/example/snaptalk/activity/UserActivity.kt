package com.example.snaptalk.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.snaptalk.R
import com.example.snaptalk.adapter.UserAdapter
import com.example.snaptalk.firebase.FirebaseService
import com.example.snaptalk.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import de.hdodenhof.circleimageview.CircleImageView

class UserActivity : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView

    var userList = ArrayList<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        FirebaseService.sharedPref = getSharedPreferences("sharedPref", MODE_PRIVATE)
        FirebaseInstallations.getInstance().id.addOnSuccessListener{
            FirebaseService.token = it
        }

        val userRecyclerView = this.findViewById<RecyclerView>(R.id.userRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL, false)

        var imgBack = findViewById<ImageView>(R.id.imgBack)
        imgBack.setOnClickListener{
            onBackPressed()
        }
        var imgProfile = findViewById<CircleImageView>(R.id.imgProfile)
        imgProfile.setOnClickListener{
            val intent = Intent(this@UserActivity, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }
        getUsersList()

    }

    fun getUsersList(){
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

        var userid = firebase.uid
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/$userid")

        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                val imgProfile = findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.imgProfile)
                val currentUser= snapshot.getValue(User::class.java)
                if(currentUser!!.profileImage == "")
                {
                    imgProfile.setImageResource(R.drawable.profile_image)
                }else{
                    Glide.with(this@UserActivity).load(currentUser!!.profileImage).into(imgProfile)
                }

                for(dataSnapShot:DataSnapshot in snapshot.children){
                    val user= dataSnapShot.getValue(User::class.java)
                    if(!user!!.userId.equals(firebase.uid)){

                        userList.add(user)
                    }
                }

                val userAdapter = UserAdapter(this@UserActivity, userList)
                userRecyclerView = findViewById(R.id.userRecyclerView)
                userRecyclerView.adapter = userAdapter

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }

           })
        }
}