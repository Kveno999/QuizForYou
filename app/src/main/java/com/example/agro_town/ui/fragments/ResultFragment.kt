package com.example.agro_town.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.agro_town.databinding.FragmentOrdersBinding
import com.google.firebase.auth.FirebaseAuth

class ResultFragment : Fragment() {




    private val args: ResultFragmentArgs by navArgs()

    private var _binding: FragmentOrdersBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        val root: View = binding.root




        val correctAnswers = args.correctAnswers
        val size = args.size
        val mFirebaseAuth = FirebaseAuth.getInstance()
        val mFirebaseUser = mFirebaseAuth.currentUser
        binding.tvName.text = mFirebaseUser!!.email

        binding.tvScore.text = "Your Score is $correctAnswers out of $size."

        binding.btnFinish.setOnClickListener {
            findNavController().navigate(ResultFragmentDirections.actionNavigationOrdersToNavigationDashboard())
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}