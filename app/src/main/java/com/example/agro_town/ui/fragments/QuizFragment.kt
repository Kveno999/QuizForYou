package com.example.agro_town.ui.fragments

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.agro_town.R
import com.example.agro_town.databinding.FragmentProductsBinding
import com.example.agro_town.models.Constans
import com.example.agro_town.models.Question
import com.example.agro_town.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_products.*

class QuizFragment : Fragment(), View.OnClickListener {



    private var mCurrentPosition: Int = 1
    private var mQuestionsList: ArrayList<Question>? = null

    private var mSelectedOptionPosition: Int = 0
    private var mCorrectAnswers: Int = 0
    private var mUserName = ""
    private lateinit var mDbRef : DatabaseReference
    private var _binding: FragmentProductsBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root



        mQuestionsList = Constans.getQuestions()

        setQuestion()

        binding.tvOptionOne.setOnClickListener(this)
        binding.tvOptionTwo.setOnClickListener(this)
        binding.tvOptionThree.setOnClickListener(this)
        binding.tvOptionFour.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)


        return root



    }
    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.tv_option_one -> {

                selectedOptionView(tv_option_one, 1)
            }

            R.id.tv_option_two -> {

                selectedOptionView(tv_option_two, 2)
            }

            R.id.tv_option_three -> {

                selectedOptionView(tv_option_three, 3)
            }

            R.id.tv_option_four -> {

                selectedOptionView(tv_option_four, 4)
            }

            R.id.btn_submit -> {

                if (mSelectedOptionPosition == 0) {

                    mCurrentPosition++

                    when {

                        mCurrentPosition <= mQuestionsList!!.size -> {

                            setQuestion()
                        }
                        else -> {
                            // START
                            findNavController().navigate(QuizFragmentDirections.actionNavigationProductsToNavigationOrders(mUserName, mCorrectAnswers,mQuestionsList!!.size))
                            // Here i tried to added correctAnswer points to database
                            mDbRef = FirebaseDatabase.getInstance().reference
                            val mAuth = FirebaseAuth.getInstance().currentUser?.uid
                            mDbRef.child("user").child(mAuth.toString()).child("points").setValue(mCorrectAnswers)
                            val mUser = User()
                            mUser.points = mCorrectAnswers

                            //
                            return
                        }
                    }
                } else {
                    val question = mQuestionsList?.get(mCurrentPosition - 1)


                    if (question!!.correctAnswer != mSelectedOptionPosition) {
                        answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                    }
                    else {
                        mCorrectAnswers++
                    }


                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)

                    if (mCurrentPosition == mQuestionsList!!.size) {
                        btn_submit.text = "FINISH"
                    } else {
                        btn_submit.text = "GO TO NEXT QUESTION"
                    }

                    mSelectedOptionPosition = 0
                }
            }
        }
    }

    private fun setQuestion() {

        val question = mQuestionsList!!.get(mCurrentPosition - 1)

        defaultOptionsView()

        if (mCurrentPosition == mQuestionsList!!.size) {
            binding.btnSubmit.text = "FINISH"
        } else {
            binding.btnSubmit.text = "SUBMIT"
        }
        with(binding){
            progressBar.progress = mCurrentPosition
            tvProgress.text = "$mCurrentPosition" + "/" + progressBar.getMax()

            tvQuestion.text = question.question
            ivImage.setImageResource(question.image)
            tvOptionOne.text = question.optionOne
            tvOptionTwo.text = question.optionTwo
            tvOptionThree.text = question.optionThree
            tvOptionFour.text = question.optionFour
        }


    }


    private fun selectedOptionView(tv: TextView, selectedOptionNum: Int) {

        defaultOptionsView()

        mSelectedOptionPosition = selectedOptionNum

        tv.setTextColor(
            Color.parseColor("#363A43")
        )
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.selected_option_border_bg
        )

    }

    private fun defaultOptionsView() {

        val options = ArrayList<TextView>()
        options.add(0, binding.tvOptionOne)
        options.add(1, binding.tvOptionTwo)
        options.add(2, binding.tvOptionThree)
        options.add(3, binding.tvOptionFour)

        for (option in options) {
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(requireContext(),
                R.drawable.default_option_border_bg
            )
        }
    }


    private fun answerView(answer: Int, drawableView: Int) {

        when (answer) {

            1 -> {
                tv_option_one.background = ContextCompat.getDrawable(
                    requireContext(),
                    drawableView
                )
            }
            2 -> {
                tv_option_two.background = ContextCompat.getDrawable(
                    requireContext(),
                    drawableView
                )
            }
            3 -> {
                tv_option_three.background = ContextCompat.getDrawable(
                    requireContext(),
                    drawableView
                )
            }
            4 -> {
                tv_option_four.background = ContextCompat.getDrawable(
                    requireContext(),
                    drawableView
                )
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}