package com.example.agro_town.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.agro_town.R
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : BaseActivity() {

    private lateinit var submit : Button
    private lateinit var email1 : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        setupActionBar()
    }


    private fun setupActionBar() {

        submit = findViewById(R.id.submitButton)
        email1 = findViewById(R.id.emailEditText2)

        submit.setOnClickListener {
            val email: String = email1.text.toString().trim { it <= ' ' }
            if (email.isEmpty()){
                showErrorSnackBar(resources.getString(R.string.error_email), true)
            }else {
                showProgressDialog("Please Wait!")
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        hideProgressDialog()
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this@ForgotPasswordActivity, "Successfully sent!", Toast.LENGTH_LONG
                            ).show()

                            finish()
                        }else {
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }

                    }
            }

        }

    }

}