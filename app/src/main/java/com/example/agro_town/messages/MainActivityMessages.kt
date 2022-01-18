package com.example.agro_town.messages


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agro_town.R
import com.example.agro_town.models.User
import com.example.agro_town.models.UserAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivityMessages : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

//    private lateinit var binding: ActivityMain2Binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)


        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#ff91a4")))

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference

        userList = ArrayList()
        adapter = UserAdapter(this, userList)

        userRecyclerView = findViewById(R.id.userRecyclerView)
        userRecyclerView.layoutManager = GridLayoutManager(this, 2)
        userRecyclerView.adapter = adapter
//        textViewPoint = findViewById(R.id.textViewPoint)

        mDbRef.child("user").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()
                for (postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(User::class.java)

                    if (mAuth.currentUser?.uid != currentUser?.id){
                        userList.add(currentUser!!)
                    }


                }
                adapter.notifyDataSetChanged()

            }


            override fun onCancelled(error: DatabaseError) {

            }

        })

//        mDbRef.child("user").child(mAuth.currentUser?.uid.toString()).child("points").get().addOnSuccessListener {
//            textViewPoint.text = it.value.toString()
//        }


    }

}