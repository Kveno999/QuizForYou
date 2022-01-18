package com.example.agro_town.models

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agro_town.R
import com.example.agro_town.messages.ChatActivity
import kotlinx.android.synthetic.main.activity_messages.view.*


class UserAdapter(val context: Context, val userList: ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
//    private lateinit var mAuth: FirebaseAuth
//    private lateinit var mDbRef: DatabaseReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.activity_messages, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserAdapter.UserViewHolder, position: Int) {
        val currentUser = userList[position]
        val userHashMap = HashMap<String, Any>()
//        mAuth = FirebaseAuth.getInstance()
//        mDbRef = FirebaseDatabase.getInstance().reference
        val points = currentUser.points.toString()



//        mDbRef.child("user").child(mAuth.uid.toString()).child("points").get().addOnSuccessListener {
//            holder.textViewPoints.text = "QuizPoints ${it.value.toString()}"
//        }
        holder.textViewPoints.text = "Points: $points"
        holder.textName.text = currentUser.firstName
        Glide.with(context).load(currentUser.image)
                .placeholder(R.drawable.placeholder)
                .into(holder.itemView.profile)

        holder.itemView.setOnClickListener{
            val intent = Intent(context, ChatActivity::class.java)

            intent.putExtra("firstname", currentUser.firstName)
            intent.putExtra("uid", currentUser.id)


            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }


    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName = itemView.findViewById<TextView>(R.id.textName)
        val textViewPoints = itemView.findViewById<TextView>(R.id.textViewPoint)
    }


}
