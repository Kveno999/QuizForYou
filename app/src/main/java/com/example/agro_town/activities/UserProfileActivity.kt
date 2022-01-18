package com.example.agro_town.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.agro_town.firestore.FirestoreClass
import com.example.agro_town.R
import com.example.agro_town.models.Constans
import com.example.agro_town.models.User
import com.example.agro_town.utils.GlideLoader
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException

class UserProfileActivity : BaseActivity(), View.OnClickListener {


    private lateinit var mUserDetails: User

    private var mSelectedImageFileUri: Uri? = null
    private var mUserProfileImageUrl: String = ""
//    private lateinit var mDbRef : DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)


        if (intent.hasExtra(Constans.EXTRA_USER_DETAILS)) {
            mUserDetails = intent.getParcelableExtra(Constans.EXTRA_USER_DETAILS)!!
        }

        first_name_et.setText(mUserDetails.firstName)
        last_name_et.setText(mUserDetails.lastName)
        mail_et.isEnabled = false
        mail_et.setText(mUserDetails.email)


        if (mUserDetails.profileCompleted == 0){
            first_name_et.isEnabled = false
            last_name_et.isEnabled = false


        }else {
            profile_tv.text = resources.getString(R.string.title_complete_profile)
            GlideLoader(this@UserProfileActivity).lodUserPicture(mUserDetails.image, img_profile)


            if (mUserDetails.mobile != 0L) {
                mobile_number_et.setText(mUserDetails.mobile.toString())
            }
            if (mUserDetails.gender == Constans.MALE) {
                rb_male.isChecked = true
            } else {
                rb_female.isChecked = true
            }

        }


        img_profile.setOnClickListener(this@UserProfileActivity)
        button_save.setOnClickListener(this@UserProfileActivity)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.img_profile -> {

                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                    == PackageManager.PERMISSION_GRANTED
                    ) {
                        Constans.showImageChooser(this)
                    } else {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        Constans.READ_STORAGE_PERMISSION_CODE)
                    }

                }

                R.id.button_save ->{


                    if (validateUserProfileDetails()) {


                        showProgressDialog("Please Wait!")


                        if (mSelectedImageFileUri != null) {
                            FirestoreClass().uploadImageToCloudStorage(this, mSelectedImageFileUri)
                            FirebaseDatabase.getInstance().reference.child("user")
                                .child(mUserDetails.id)
                                .runTransaction(object : Transaction.Handler {
                                    override fun doTransaction(currentData: MutableData): Transaction.Result {
                                        val user: User = currentData.getValue(User::class.java)!!
                                        user.image = mSelectedImageFileUri.toString()
                                        currentData.value = user
                                        return Transaction.success(currentData)
                                    }


                                    override fun onComplete(
                                        error: DatabaseError?,
                                        committed: Boolean,
                                        currentData: DataSnapshot?
                                    ) {

                                    }

                                })

                            val database = Firebase.database.reference
                            database.child("user").child(mUserDetails.id).child("image").setValue(mSelectedImageFileUri.toString())
                        }


                        else {
                            updateUserProfileDetails()

                        }


                    }


                }
            }
        }
    }
//    data class Image (var image: String)


    private fun updateUserProfileDetails() {


        val userHashMap = HashMap<String, Any>()

        val firstName = first_name_et.text.toString().trim { it <= ' '}
        if (firstName != mUserDetails.firstName) {
            userHashMap[Constans.FIRST_NAME] = firstName
        }

        val lastName = last_name_et.text.toString().trim { it <= ' '}
        if (lastName != mUserDetails.lastName) {
            userHashMap[Constans.LAST_NAME] = lastName
        }

        val mobileNumber = mobile_number_et.text.toString().trim { it <= ' '}

        val gender = if(rb_male.isChecked) {
            Constans.MALE
        } else {
            Constans.FEMALE
        }


        if (mUserProfileImageUrl.isNotEmpty()) {
            userHashMap[Constans.IMAGE] = mUserProfileImageUrl
        }


        if (mobileNumber.isNotEmpty() && mobileNumber != mUserDetails.mobile.toString()) {
            userHashMap[Constans.MOBILE] = mobileNumber.toLong()
        }
        if (gender.isNotEmpty() && gender != mUserDetails.gender) {
            userHashMap[Constans.GENDER] = gender
        }


        userHashMap[Constans.GENDER] = gender


        userHashMap[Constans.COMPLETE_PROFILE] = 1


        FirestoreClass().updateUserProfileData(this, userHashMap)

    }



    fun userProfileUpdateSuccess() {
        hideProgressDialog()

        Toast.makeText(this@UserProfileActivity,
        resources.getString(R.string.msg_profile_update_success),
            Toast.LENGTH_SHORT
        ).show()


        startActivity(Intent(this@UserProfileActivity, DashboardActivity::class.java))
        finish()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constans.READ_STORAGE_PERMISSION_CODE) {

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               Constans.showImageChooser(this)

            } else {

                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()

            }

        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constans.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {

                        mSelectedImageFileUri = data.data!!

                        GlideLoader(this).lodUserPicture(mSelectedImageFileUri!!, img_profile)

                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(this@UserProfileActivity,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        )
                            .show()

                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED){

            Log.e("Result Cancelled", "Image Selection Cancelled")

        }
    }
    private fun validateUserProfileDetails(): Boolean {
        return when {
            TextUtils.isEmpty(mobile_number_et.text.toString().trim{ it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_mobile_number), true)
                false
            }
            else -> {
                true
            }
        }
    }

    fun imageUploadSuccess(imageURL: String) {
        //hideProgressDialog()

        mUserProfileImageUrl = imageURL

        updateUserProfileDetails()

    }



}

