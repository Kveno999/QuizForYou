package com.example.agro_town.ui.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.agro_town.R
import com.example.agro_town.databinding.FragmentDashboardBinding

import com.example.agro_town.messages.MainActivityMessages
import com.example.agro_town.models.User

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_messages.view.*


class HomeFragment : Fragment() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setHasOptionsMenu(true)


    }

    //private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference


    private val binding get() = _binding!!

    private lateinit var mProgressDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ///dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val mFirebaseAuth = FirebaseAuth.getInstance()
        val mFirebaseUser = mFirebaseAuth.currentUser
        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference
        val mUser = User()

        binding.tvDescription.text = mFirebaseUser!!.email
        binding.btnStart.setOnClickListener{
            findNavController().navigate(HomeFragmentDirections.actionNavigationDashboardToNavigationProducts())
        }



        mDbRef.child("user").child(mAuth.uid.toString()).child("image").get().addOnSuccessListener {
            Glide.with(requireContext()).load(it.value.toString())
            .placeholder(R.drawable.placeholder)
                .into(binding.imageViewProfile)
        }

        return root

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {

            R.id.action_messages -> {
                startActivity(Intent(activity, MainActivityMessages::class.java))


                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}