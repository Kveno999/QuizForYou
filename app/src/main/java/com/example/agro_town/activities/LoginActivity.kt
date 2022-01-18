package com.example.agro_town.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.agro_town.firestore.FirestoreClass
import com.example.agro_town.R
import com.example.agro_town.models.Constans
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val forgotPass = findViewById<TextView>(R.id.forgotPassTextView)
        val loginButton = findViewById<TextView>(R.id.buttonLogin)
        val registerButton = findViewById<TextView>(R.id.doNotHaveAcc)

        forgotPass.setOnClickListener(this)
        loginButton.setOnClickListener(this)
        registerButton.setOnClickListener(this)


    }

    fun userLoggedInSuccess(user: com.example.agro_town.models.User) {

        hideProgressDialog()

        if (user.profileCompleted == 0) {
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(Constans.EXTRA_USER_DETAILS, user)
            startActivity(intent)
        } else {
            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
        }
        finish()


    }


    override fun onClick(view: View?) {
        if (view != null) {
            when(view.id) {

                R.id.forgotPassTextView -> {

                    intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent)

                }

                R.id.buttonLogin -> {

                    logInRegisteredUser()

                }

                R.id.doNotHaveAcc -> {
                    intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
    private fun validateLoginDetials(): Boolean {
        val email = findViewById<EditText>(R.id.emailEditText)
        val password = findViewById<EditText>(R.id.passwordEditText)
        return when {
            TextUtils.isEmpty(email.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.error_first_name), true)
                false
            }
            TextUtils.isEmpty(password.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.error_first_name), true)
                false
            }
            else -> {
                true

            }
        }
    }

    private fun logInRegisteredUser() {

        val email = findViewById<EditText>(R.id.emailEditText)
        val password = findViewById<EditText>(R.id.passwordEditText)

        if (validateLoginDetials()) {

            showProgressDialog("Please Wait!")

            val email1 = email.text.toString().trim { it <= ' ' }
            val password1 = password.text.toString().trim { it <= ' ' }


            FirebaseAuth.getInstance().signInWithEmailAndPassword(email1, password1)
                .addOnCompleteListener { task ->



                    if (task.isSuccessful) {

                        FirestoreClass().getUserDetails(this@LoginActivity)
                    } else {
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }

                }


        }

    }

}