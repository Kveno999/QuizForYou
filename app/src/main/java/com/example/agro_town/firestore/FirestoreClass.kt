package com.example.agro_town.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.example.agro_town.activities.LoginActivity
import com.example.agro_town.activities.RegisterActivity
import com.example.agro_town.activities.SettingsActivity
import com.example.agro_town.activities.UserProfileActivity
import com.example.agro_town.models.Constans
import com.example.agro_town.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlin.collections.HashMap

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()



    fun registerUser(activity: RegisterActivity, userInfo: User) {
        mFireStore.collection(Constans.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error While Registering the user",
                    e
                )
            }
    }

    fun getCurrentUserID(): String {

        val currentUser = FirebaseAuth.getInstance().currentUser


        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return  currentUserID

    }

    fun getUserDetails(activity: Activity) {
        mFireStore.collection(Constans.USERS)

            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.i(activity.javaClass.simpleName, document.toString())

                val user = document.toObject((User::class.java))!!


                val sharedPreferences =
                    activity.getSharedPreferences(
                        Constans.AGROTOWNPREFERENCES,
                        Context.MODE_PRIVATE
                    )
                val editor: SharedPreferences.Editor = sharedPreferences.edit()

                editor.putString(
                    Constans.LOGGED_IN_USERNAME,
                    "${user.firstName} ${user.lastName}"
                )
                editor.apply()


                when(activity) {
                    is LoginActivity -> {

                        activity.userLoggedInSuccess(user)

                    }
                    is SettingsActivity -> {
                        activity.userDetailsSuccess(user)
                    }
                }


            }
            .addOnFailureListener { e ->
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                    is SettingsActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }
    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {

        mFireStore.collection(Constans.USERS).document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when (activity) {
                    is UserProfileActivity -> {


                        activity.userProfileUpdateSuccess()

                    }
                }
            }
            .addOnFailureListener { e->
                when (activity) {
                    is UserProfileActivity -> {

                        activity.hideProgressDialog()

                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error While updating the user details.",
                    e
                )
            }

    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?) {

        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            Constans.USER_PROFILE_IMAGE + System.currentTimeMillis() + "."
                    + Constans.getFileExtension(
                activity,
                imageFileURI
        )

        )

        sRef.putFile(imageFileURI!!).addOnSuccessListener { taskSnapshot ->

            Log.e(
                "Firebase Image URL",
                taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
            )

            taskSnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { uri ->
                    Log.e("Downloadable Image URL", uri.toString())
                    /*sRef.downloadUrl.addOnSuccessListener {
                        Log.e("RegisterActivity" , "File Location: $it")
                    }*/

                    when (activity) {
                        is UserProfileActivity -> {
                            activity.imageUploadSuccess(uri.toString())
                        }
                    }


                }

                .addOnFailureListener { exception ->

                    when (activity) {
                        is UserProfileActivity -> {
                            activity.hideProgressDialog()
                        }

                    }


                    Log.e(
                        activity.javaClass.simpleName,
                        exception.message,
                        exception
                    )

                }
        }


    }


}