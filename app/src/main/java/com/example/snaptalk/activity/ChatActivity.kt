package com.example.snaptalk.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat.MessagingStyle.Message
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.snaptalk.R
import com.example.snaptalk.RetrofitInstance
import com.example.snaptalk.adapter.ChatAdapter
import com.example.snaptalk.adapter.UserAdapter
import com.example.snaptalk.model.Chat
import com.example.snaptalk.model.NotificationData
import com.example.snaptalk.model.PushNotification
import com.example.snaptalk.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {
    private var firebaseUser: FirebaseUser? = null
    private var reference: DatabaseReference? = null
    private lateinit var imgProfile: de.hdodenhof.circleimageview.CircleImageView
    private lateinit var tvUserName: TextView
    private lateinit var imgBack: ImageView
    private lateinit var btnSendMessage: ImageButton
    private lateinit var etMessage: EditText
    private lateinit var chatRecyclerView: RecyclerView
    var chatList = ArrayList<Chat>()

    var topic = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Initialize the chatRecyclerView here after setContentView
        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        chatRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        imgProfile = findViewById(R.id.imgProfile)
        tvUserName = findViewById(R.id.tvUserName)
        imgBack = findViewById(R.id.imgBack)

        var intent = getIntent()
        var userId = intent.getStringExtra("userId")
        var userName = intent.getStringExtra("userName")

        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("users").child(userId!!)
        imgBack.setOnClickListener {
            onBackPressed()
        }
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                tvUserName.text = user!!.userName
                if (user!!.profileImage == "") {
                    imgProfile.setImageResource(R.drawable.profile_image)
                } else {
                    Glide.with(this@ChatActivity).load(user.profileImage).into(imgProfile)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        btnSendMessage = findViewById(R.id.btnSendMessage)
        etMessage = findViewById(R.id.etMessage)
        btnSendMessage.setOnClickListener {
            var message: String = etMessage.text.toString()
            if (message.isEmpty()) {
                Toast.makeText(applicationContext, "message is empty", Toast.LENGTH_SHORT).show()
                etMessage.setText("")
            } else {
                sendMessage(firebaseUser!!.uid, userId, message)
                etMessage.setText("")
                topic = "/topics/$userId"
                PushNotification(NotificationData( userName!!, message),
                topic).also {
                    sendNotification(it)
                }
            }
        }
        readMessage(firebaseUser!!.uid, userId)

    }

    private fun sendMessage(senderId: String, receiverId: String, message: String) {
        var reference: DatabaseReference? = FirebaseDatabase.getInstance().getReference()

        var hashMap: HashMap<String, String> = HashMap()
        hashMap.put("senderId", senderId)
        hashMap.put("receiverId", receiverId)
        hashMap.put("message", message)

        reference!!.child("Chat").push().setValue(hashMap)
        etMessage.text.clear()

    }

    fun readMessage(senderId: String, receiverId: String) {
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Chat")

        databaseReference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for(dataSnapShot:DataSnapshot in snapshot.children) {
                    val chat = dataSnapShot.getValue(Chat::class.java)
                    if (chat!!.senderId.equals(senderId) && chat!!.receiverId.equals(receiverId) ||
                        chat!!.senderId.equals(receiverId) && chat!!.receiverId.equals(senderId)
                    ) {
                        chatList.add(chat)
                    }
                }

                val chatAdapter = ChatAdapter(this@ChatActivity, chatList)
                chatRecyclerView.adapter = chatAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
            })
       }

    private fun sendNotification(notification:PushNotification) =CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful){
                runOnUiThread{
                    //Toast.makeText(this@ChatActivity , "Response ${Gson().toJson(response)}", Toast.LENGTH_SHORT).show()
                }

            }
            else{
                runOnUiThread {
                    Toast.makeText(this@ChatActivity , response.errorBody().toString(), Toast.LENGTH_SHORT).show()
                }

            }
        }
        catch(e:Exception){
            runOnUiThread {
                Toast.makeText(this@ChatActivity, e.message,Toast.LENGTH_SHORT).show()
            }

        }
    }
}