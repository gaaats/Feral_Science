package com.quizapp.quizfoirtopics

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.quizapp.quizfoirtopics.databinding.FragmentEnterBinding
import com.quizapp.quizfoirtopics.databinding.FragmentGeographyBinding
import com.quizapp.quizfoirtopics.quizentity.MyInterceptor
import com.quizapp.quizfoirtopics.quizentity.QuizServiceAPI
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class EnterFragment : Fragment() {

    var counter = 0.1f
    var diff = 0.1f
    private var _binding: FragmentEnterBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("FragmentStartBinding = null")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        try {
            cycleUpAndDovnAlpha()

            binding.btnImgSettingsSome.setOnClickListener {
                findNavController().navigate(R.id.action_enterFragment_to_someSettingsFragment)
            }
            binding.btnAddInfo.setOnClickListener {
                findNavController().navigate(R.id.action_enterFragment_to_aboutAppInfoFragment)
            }
            binding.imgGeographyQuiz.setOnClickListener {
                findNavController().navigate(R.id.action_enterFragment_to_geographyFragment)
            }
            binding.imgMathematicsQuizz.setOnClickListener {
                findNavController().navigate(R.id.action_enterFragment_to_mathematicsFragment)
            }
            binding.imgMusicQuiz.setOnClickListener {
                findNavController().navigate(R.id.action_enterFragment_to_musicFragment)
            }
            binding.imgSportQuiz.setOnClickListener {
                findNavController().navigate(R.id.action_enterFragment_to_sportsFragment)
            }


        } catch (e: Exception) {
            snackBarError()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun cycleUpAndDovnAlpha() {
        lifecycleScope.launch {
            while (true) {
                Log.d("enter_frag", "i am in loop")
                Log.d("enter_frag", "counter is ${counter}")
                binding.tvTitleChooseQuiz.alpha = counter
                if (counter >= 1f) {
                    diff = -0.1f
                }
                if (counter <= 0.1f) {
                    diff = 0.1f
                }
                delay(100)
                counter += diff
            }
        }
    }

    private fun snackBarError() {
        Snackbar.make(
            binding.root,
            "Oops something went wrong, please try again...",
            Snackbar.LENGTH_LONG
        ).show()
        requireActivity().onBackPressed()
    }

    private fun initAlertDialogForExit() {
        AlertDialog.Builder(requireContext())
            .setTitle("Exit")
            .setMessage("Current data will not be saved, are you really want to log out ?")
            .setPositiveButton("Yes, Exit") { _, _ ->
                requireActivity().onBackPressed()
            }
            .setNegativeButton("Deny") { _, _ ->
            }
            .setCancelable(true)
            .create()
            .show()
    }

}