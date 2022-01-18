package com.example.agro_town.activities


import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import com.example.agro_town.firestore.FirestoreClass
import com.example.agro_town.R
import com.example.agro_town.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.util.*

class RegisterActivity : BaseActivity() {


    private lateinit var regETFirstName : EditText
    private lateinit var regETLastName: EditText
    private lateinit var regETEmail : EditText
    private lateinit var regETPassword : EditText
    private lateinit var regETPassword2 : EditText
    private lateinit var terms : CheckBox
    private lateinit var registration : Button
    private lateinit var mDbRef : DatabaseReference





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)



        regETFirstName = findViewById(R.id.regETFirstName)
        regETLastName = findViewById(R.id.regETLastName)
        regETEmail = findViewById(R.id.regETEmail)
        regETPassword = findViewById(R.id.regETPassword)
        regETPassword2 = findViewById(R.id.regETPassword2)
        terms = findViewById(R.id.terms)
        registration = findViewById(R.id.registrationButton)



        val haveAccount = findViewById<TextView>(R.id.alreadyHaveAnAccount)


        haveAccount.setOnClickListener {
            onBackPressed()
        }
        registration.setOnClickListener {
            registerUser()
        }


    }

    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(regETFirstName.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.error_first_name), true)
                false
            }
            TextUtils.isEmpty(regETLastName.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.error_last_name), true)
                false
            }
            TextUtils.isEmpty(regETEmail.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.error_email), true)
                false
            }
            TextUtils.isEmpty(regETPassword.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.error_password), true)
                false
            }
            TextUtils.isEmpty(regETPassword2.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.error_password2), true)
                false
            }
            !terms.isChecked -> {
                showErrorSnackBar(resources.getString(R.string.error_checkbox), true)
                false
            }
            else -> {
                true
            }


        }
    }

    private fun registerUser() {


        showProgressDialog("Please Wait!")

        if (validateRegisterDetails()) {

            val email: String = regETEmail.text.toString().trim { it <= ' ' }
            val password: String = regETPassword.text.toString().trim { it <= ' ' }
            val name: String = regETFirstName.text.toString().trim { it <= ' ' }



            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->

                        hideProgressDialog()

                        if (task.isSuccessful) {

                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            val user = User(
                                firebaseUser.uid,
                                regETFirstName.text.toString().trim { it <= ' ' },
                                regETLastName.text.toString().trim { it <= ' ' },
                                regETEmail.text.toString().trim { it <= ' ' }

                            )


                            FirestoreClass().registerUser(this@RegisterActivity, user)
                            addUserToDatabase(name, email, firebaseUser.uid)


                        } else {

                            hideProgressDialog()
                            showErrorSnackBar(task.exception!!.message.toString(), true)

                        }


                    }
                )

        }

    }


    private fun addUserToDatabase(firstName: String, email: String, id: String){
        mDbRef = FirebaseDatabase.getInstance().reference
        mDbRef.child("user").child(id).setValue(User(firstName, email, id))

    }


    fun userRegistrationSuccess() {
        hideProgressDialog()




        Toast.makeText(
            this@RegisterActivity, "You are registered successfully!",
                Toast.LENGTH_SHORT
            ).show()
    }




}