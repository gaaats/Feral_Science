package com.quizapp.quizfoirtopics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.quizapp.quizfoirtopics.databinding.FragmentMusicBinding
import com.quizapp.quizfoirtopics.databinding.FragmentSportsBinding
import com.quizapp.quizfoirtopics.quizentity.MyInterceptor
import com.quizapp.quizfoirtopics.quizentity.QuizServiceAPI
import com.quizapp.quizfoirtopics.utilitas.Constances
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SportsFragment : Fragment() {
    private var _binding: FragmentSportsBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("FragmentStartBinding = null")

    private val listImagesLogoSport = listOf(
        R.drawable.bodybuilding,
        R.drawable.polo,
        R.drawable.vaterpolo,
        R.drawable.tennis,
        R.drawable.basketballplayer,
        R.drawable.football,
        R.drawable.sport,
        R.drawable.running,
        R.drawable.sports,
    )

    private var currentQuestion = ""
    private var currentAns = ""

    private val loading = 1
    private val correct = 2
    private val fail = 3

    private val listFakeAnsMusic = listOf(
        "football",
        "tennis",
        "5998.5",
        "Ronaldo",
        "Messi",
        "Polo",
        "First time",
        "One month",
        "Neymar",
        "Christiano",
        "Manchester",
        "Chelsea",
        "Liverpool",
        "Berlin",
        "London Arsenal",
        "Football Champ",
        "Champion League",
        "Box",
        "1 000 m",
        "3 450 m",
        "Shape running",
        "Long jumping",
        "Solo box",
        "band",
    )

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(QuizServiceAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private val client = OkHttpClient.Builder().apply {
        addInterceptor(MyInterceptor())
    }.build()

    val api: QuizServiceAPI by lazy {
        retrofit.create(QuizServiceAPI::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSportsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initProgBar(loading)

        binding.btnImgRulesSport.setOnClickListener {
            findNavController().navigate(R.id.action_sportsFragment_to_aboutAppInfoFragment)
        }

        try {
            binding.btnImgExitSport.setOnClickListener {
                initAlertDialogForExit()
            }
            binding.btnAns1Sport.setOnClickListener {
                sendAnsFromUser(binding.btnAns1Sport.text.toString())
            }
            binding.btnAns2Sport.setOnClickListener {
                sendAnsFromUser(binding.btnAns2Sport.text.toString())
            }
            binding.btnAns3Sport.setOnClickListener {
                sendAnsFromUser(binding.btnAns3Sport.text.toString())
            }
            binding.btnAns4Sport.setOnClickListener {
                sendAnsFromUser(binding.btnAns4Sport.text.toString())
            }

        } catch (e: Exception) {
            snackBarError()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun sendAnsFromUser(textAns: String) {
        if (currentAns == textAns) {
            lifecycleScope.launch {
                initProgBar(correct)
            }
        } else {
            initProgBar(fail)
        }
    }

    private fun loadList() {
        lifecycleScope.launch {
            try {
                val result = api.getQuestion(category = "music")
                if (result.isSuccessful) {
                    currentQuestion = result.body()!![0].question!!
                    currentAns = result.body()!![0].answer!!

                    binding.tvQuestionTextSport.text = currentQuestion
                    binding.btnAns1Sport.text = currentAns
                    binding.btnAns2Sport.text = listFakeAnsMusic.random()
                    binding.btnAns3Sport.text = listFakeAnsMusic.random()
                    binding.btnAns4Sport.text = listFakeAnsMusic.random()
                    binding.imgLogoQuestionSport.setImageResource(listImagesLogoSport.random())

                } else {
                    snackBarError()
                }
            } catch (e: Exception) {
                snackBarError()
            }
        }
    }


    private fun snackBarError() {
        Snackbar.make(
            binding.root,
            "There is some error, try again",
            Snackbar.LENGTH_LONG
        ).show()
        requireActivity().onBackPressed()
    }

    private fun initAlertDialogForExit() {
        AlertDialog.Builder(requireContext())
            .setTitle("Exit")
            .setMessage("Are you definitely want to log out, the current data will not be saved?")
            .setPositiveButton("Yes, Exit") { _, _ ->
                requireActivity().onBackPressed()
            }
            .setNegativeButton("Deny") { _, _ ->
            }
            .setCancelable(true)
            .create()
            .show()
    }

    private fun initProgBar(kindOfAnim: Int) {
        lifecycleScope.launch {
            binding.tvTitleQuizSport.visibility = View.GONE
            binding.btnImgExitSport.visibility = View.GONE
            binding.imgLogoQuestionSport.visibility = View.GONE
            binding.tvQuestionTextSport.visibility = View.GONE
            binding.tvDovnText.visibility = View.GONE
            binding.btnAns1Sport.visibility = View.GONE
            binding.btnAns2Sport.visibility = View.GONE
            binding.btnAns3Sport.visibility = View.GONE
            binding.btnAns4Sport.visibility = View.GONE
            binding.btnImgRulesSport.visibility = View.GONE

            when (kindOfAnim) {
                loading -> {
                    binding.lottieAnimLoading.visibility = View.VISIBLE
                }
                correct -> {
                    SportsFragmentDirections.actionSportsFragmentToCorrectAnsFragment(typeQuiz = Constances.TYPE_QUIZ_GEO)
                        .also {
                            findNavController().navigate(it)
                        }
                }
                fail -> {
                    SportsFragmentDirections.actionSportsFragmentToVrongAnsFragment(typeQuizz = Constances.TYPE_QUIZ_GEO)
                        .also {
                            findNavController().navigate(it)
                        }
                }
            }

            loadList()

            delay(1100)

            binding.tvTitleQuizSport.visibility = View.VISIBLE
            binding.btnImgExitSport.visibility = View.VISIBLE
            binding.imgLogoQuestionSport.visibility = View.VISIBLE
            binding.tvQuestionTextSport.visibility = View.VISIBLE
            binding.tvDovnText.visibility = View.VISIBLE
            binding.btnAns1Sport.visibility = View.VISIBLE
            binding.btnAns2Sport.visibility = View.VISIBLE
            binding.btnAns3Sport.visibility = View.VISIBLE
            binding.btnAns4Sport.visibility = View.VISIBLE
            binding.btnImgRulesSport.visibility = View.VISIBLE

            binding.lottieAnimLoading.visibility = View.GONE

        }
    }
}