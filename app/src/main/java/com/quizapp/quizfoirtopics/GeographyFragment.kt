package com.quizapp.quizfoirtopics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.quizapp.quizfoirtopics.databinding.FragmentGeographyBinding
import com.quizapp.quizfoirtopics.quizentity.MyInterceptor
import com.quizapp.quizfoirtopics.quizentity.QuizServiceAPI
import com.quizapp.quizfoirtopics.utilitas.Constances
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class GeographyFragment : Fragment() {
    private var _binding: FragmentGeographyBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("FragmentStartBinding = null")

    private val listImagesLogo = listOf(
        R.drawable.teacher,
        R.drawable.geography,
        R.drawable.earth,
        R.drawable.planet,
        R.drawable.position,
        R.drawable.globe,
        R.drawable.geography_2,
        R.drawable.geography_1,
        R.drawable.map,
    )

    private var currentQuestion = ""
    private var currentAns = ""

    private val loading = 1
    private val correct = 2
    private val fail = 3

    private val listFakeAns = listOf(
        "British",
        "Ukraine",
        "56262.54",
        "Mountain",
        "River",
        "2000000 mln",
        "Never",
        "Sunday",
        "Dnipro",
        "Dunay",
        "Open sea",
        "North Ocean",
        "Atlantic Ocean",
        "Island",
        "Black mountain",
        "Volcano",
        "River flood",
        "Pathetic ocean",
        "USA",
        "Georgia",
        "Cratos",
        "Netherlands",
        "Bulgaria",
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
        _binding = FragmentGeographyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initProgBar(loading)

        binding.btnImgRulesGeo.setOnClickListener {
            findNavController().navigate(R.id.action_geographyFragment_to_aboutAppInfoFragment3)
        }

        try {
            binding.btnImgExitGeo.setOnClickListener {
                initAlertDialogForExit()
            }
            binding.btnAns1Geo.setOnClickListener {
                sendAnsFromUser(binding.btnAns1Geo.text.toString())
            }
            binding.btnAns2Geo.setOnClickListener {
                sendAnsFromUser(binding.btnAns2Geo.text.toString())
            }
            binding.btnAns3Geo.setOnClickListener {
                sendAnsFromUser(binding.btnAns3Geo.text.toString())
            }
            binding.btnAns4Geo.setOnClickListener {
                sendAnsFromUser(binding.btnAns4Geo.text.toString())
            }
//            loadList()


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
                val result = api.getQuestion()
                if (result.isSuccessful) {
                    currentQuestion = result.body()!![0].question!!
                    currentAns = result.body()!![0].answer!!

                    binding.tvQuestionTextGeo.text = currentQuestion
                    binding.btnAns1Geo.text = currentAns
                    binding.btnAns2Geo.text = listFakeAns.random()
                    binding.btnAns3Geo.text = listFakeAns.random()
                    binding.btnAns4Geo.text = listFakeAns.random()
                    binding.imgLogoQuestionGeo.setImageResource(listImagesLogo.random())

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
            binding.tvTitleQuizGeo.visibility = View.GONE
            binding.btnImgExitGeo.visibility = View.GONE
            binding.imgLogoQuestionGeo.visibility = View.GONE
            binding.tvQuestionTextGeo.visibility = View.GONE
            binding.tvDovnText.visibility = View.GONE
            binding.btnAns1Geo.visibility = View.GONE
            binding.btnAns2Geo.visibility = View.GONE
            binding.btnAns3Geo.visibility = View.GONE
            binding.btnAns4Geo.visibility = View.GONE
            binding.btnImgRulesGeo.visibility = View.GONE

            when (kindOfAnim) {
                loading -> {
                    binding.lottieAnimLoading.visibility = View.VISIBLE
                }
                correct -> {
                    GeographyFragmentDirections.actionGeographyFragmentToCorrectAnsFragment(typeQuiz = Constances.TYPE_QUIZ_GEO)
                        .also {
                            findNavController().navigate(it)
                        }
                }
                fail -> {
                    GeographyFragmentDirections.actionGeographyFragmentToVrongAnsFragment(typeQuizz = Constances.TYPE_QUIZ_GEO)
                        .also {
                            findNavController().navigate(it)
                        }
                }
            }

            loadList()

            delay(1100)

            binding.tvTitleQuizGeo.visibility = View.VISIBLE
            binding.btnImgExitGeo.visibility = View.VISIBLE
            binding.imgLogoQuestionGeo.visibility = View.VISIBLE
            binding.tvQuestionTextGeo.visibility = View.VISIBLE
            binding.tvDovnText.visibility = View.VISIBLE
            binding.btnAns1Geo.visibility = View.VISIBLE
            binding.btnAns2Geo.visibility = View.VISIBLE
            binding.btnAns3Geo.visibility = View.VISIBLE
            binding.btnAns4Geo.visibility = View.VISIBLE
            binding.btnImgRulesGeo.visibility = View.VISIBLE

            binding.lottieAnimLoading.visibility = View.GONE

        }
    }
}