package com.example.agro_town.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.agro_town.firestore.FirestoreClass
import com.example.agro_town.R
import com.example.agro_town.models.Constans
import com.example.agro_town.models.User
import com.example.agro_town.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_user_profile.*

class SettingsActivity : BaseActivity(), View.OnClickListener{

    private lateinit var mUserDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        button_logout.setOnClickListener(this)
        edit_settings.setOnClickListener(this)
    }

    private fun getUserDetails() {
        showProgressDialog("Please Wait!")
        FirestoreClass().getUserDetails(this)
    }
    fun userDetailsSuccess(user: User) {
        mUserDetails = user


        hideProgressDialog()

        GlideLoader(this@SettingsActivity).lodUserPicture(user.image, img_profile_settings)
        first_name_settings.text = "${user.firstName} ${user.lastName}"
        gender_settings.text = user.gender
        email_settings.text = user.email
        number_settings.text = "${user.mobile}"

    }
    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    override fun onClick(v: View?) {

        if (v != null) {
            when(v.id){
                R.id.edit_settings ->{
                    val intent = Intent(this@SettingsActivity, UserProfileActivity::class.java)
                    intent.putExtra(Constans.EXTRA_USER_DETAILS, mUserDetails)
                    startActivity(intent)

                }


                R.id.button_logout ->{
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}