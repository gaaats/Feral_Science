package com.quizapp.quizfoirtopics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.quizapp.quizfoirtopics.databinding.FragmentCorrectAnsBinding
import com.quizapp.quizfoirtopics.databinding.FragmentEnterBinding
import com.quizapp.quizfoirtopics.utilitas.Constances

class CorrectAnsFragment : Fragment() {
    private var _binding: FragmentCorrectAnsBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("FragmentStartBinding = null")

    private val args: CorrectAnsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCorrectAnsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val currentType = args.typeQuiz

        try {
            binding.btnExit.setOnClickListener {
                initAlertDialogForExitToEnterFrag()
            }
            when (currentType) {
                Constances.TYPE_QUIZ_GEO -> {
                    binding.btnNextQuestion.setOnClickListener {
                        findNavController().navigate(R.id.action_correctAnsFragment_to_geographyFragment)
                    }
                }
                Constances.TYPE_QUIZ_MATH -> {

                    binding.btnNextQuestion.setOnClickListener {
                        findNavController().navigate(R.id.action_correctAnsFragment_to_mathematicsFragment)
                    }

                }
                Constances.TYPE_QUIZ_MUSIC -> {

                    binding.btnNextQuestion.setOnClickListener {
                        findNavController().navigate(R.id.action_correctAnsFragment_to_musicFragment)
                    }

                }
                Constances.TYPE_QUIZ_SPORT -> {
                    binding.btnNextQuestion.setOnClickListener {
                        findNavController().navigate(R.id.action_correctAnsFragment_to_sportsFragment)
                    }
                }
            }

        } catch (e: Exception) {
            snackBarError()
        }

        super.onViewCreated(view, savedInstanceState)
    }


    private fun snackBarError() {
        Snackbar.make(
            binding.root,
            "Oops something went wrong, please try again...",
            Snackbar.LENGTH_LONG
        ).show()
        requireActivity().onBackPressed()
    }

    private fun initAlertDialogForExitToEnterFrag() {
        AlertDialog.Builder(requireContext())
            .setTitle("Exit")
            .setMessage("Are you definitely want to log out, the current data will not be saved?")
            .setPositiveButton("Yes, Exit") { _, _ ->
                findNavController().navigate(R.id.action_correctAnsFragment_to_enterFragment)
            }
            .setNegativeButton("Deny") { _, _ ->
            }
            .setCancelable(true)
            .create()
            .show()
    }

}