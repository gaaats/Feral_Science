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
import com.quizapp.quizfoirtopics.databinding.FragmentMathematicsBinding
import com.quizapp.quizfoirtopics.quizentity.MyInterceptor
import com.quizapp.quizfoirtopics.quizentity.QuizServiceAPI
import com.quizapp.quizfoirtopics.utilitas.Constances
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MathematicsFragment : Fragment() {
    private var _binding: FragmentMathematicsBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("FragmentStartBinding = null")

    private val listImagesLogoMath = listOf(
        R.drawable.calculator,
        R.drawable.calculator_1,
        R.drawable.study,
        R.drawable.venn_diagram,
        R.drawable.math,
        R.drawable.geometry,
        R.drawable.think,
    )

    private var currentQuestion = ""
    private var currentAns = ""

    private val loading = 1
    private val correct = 2
    private val fail = 3

    private val listFakeAns = listOf(
        "Division",
        "Multiplication",
        "123987",
        "Addition",
        "Subtraction",
        "Add",
        "8,234",
        "Divide by",
        "Subtract",
        "Multiply by",
        "Division",
        "0,25",
        "Total",
        "3675",
        "Product",
        "Difference",
        "3,14",
        "Division",
        "Quotient",
        "Remainder",
        "Derivative of",
        "Even number",
        "Common denominator",
        "Decimal number",
        "Square root of",
        "Percentage",
        "Cube root of",
        "Fraction",
        "Numerator",
        "Denominator",
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
        _binding = FragmentMathematicsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initProgBar(loading)

        binding.btnImgRulesMath.setOnClickListener {
            findNavController().navigate(R.id.action_mathematicsFragment_to_aboutAppInfoFragment)
        }

        try {
            binding.btnImgExitMath.setOnClickListener {
                initAlertDialogForExit()
            }
            binding.btnAns1Math.setOnClickListener {
                sendAnsFromUser(binding.btnAns1Math.text.toString())
            }
            binding.btnAns2Math.setOnClickListener {
                sendAnsFromUser(binding.btnAns2Math.text.toString())
            }
            binding.btnAns3Math.setOnClickListener {
                sendAnsFromUser(binding.btnAns3Math.text.toString())
            }
            binding.btnAns4Math.setOnClickListener {
                sendAnsFromUser(binding.btnAns4Math.text.toString())
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
                val result = api.getQuestion(category = "mathematics")
                if (result.isSuccessful) {
                    currentQuestion = result.body()!![0].question!!
                    currentAns = result.body()!![0].answer!!

                    binding.tvQuestionTextMath.text = currentQuestion
                    binding.btnAns1Math.text = currentAns
                    binding.btnAns2Math.text = listFakeAns.random()
                    binding.btnAns3Math.text = listFakeAns.random()
                    binding.btnAns4Math.text = listFakeAns.random()
                    binding.imgLogoQuestionMath.setImageResource(listImagesLogoMath.random())

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
            binding.tvTitleQuizMath.visibility = View.GONE
            binding.btnImgExitMath.visibility = View.GONE
            binding.imgLogoQuestionMath.visibility = View.GONE
            binding.tvQuestionTextMath.visibility = View.GONE
            binding.tvDovnText.visibility = View.GONE
            binding.btnAns1Math.visibility = View.GONE
            binding.btnAns2Math.visibility = View.GONE
            binding.btnAns3Math.visibility = View.GONE
            binding.btnAns4Math.visibility = View.GONE
            binding.btnImgRulesMath.visibility = View.GONE

            when (kindOfAnim) {
                loading -> {
                    binding.lottieAnimLoading.visibility = View.VISIBLE
                }
                correct -> {
                    MathematicsFragmentDirections.actionMathematicsFragmentToCorrectAnsFragment(typeQuiz = Constances.TYPE_QUIZ_GEO)
                        .also {
                            findNavController().navigate(it)
                        }
                }
                fail -> {
                    MathematicsFragmentDirections.actionMathematicsFragmentToVrongAnsFragment(typeQuizz = Constances.TYPE_QUIZ_GEO)
                        .also {
                            findNavController().navigate(it)
                        }
                }
            }

            loadList()

            delay(1100)

            binding.tvTitleQuizMath.visibility = View.VISIBLE
            binding.btnImgExitMath.visibility = View.VISIBLE
            binding.imgLogoQuestionMath.visibility = View.VISIBLE
            binding.tvQuestionTextMath.visibility = View.VISIBLE
            binding.tvDovnText.visibility = View.VISIBLE
            binding.btnAns1Math.visibility = View.VISIBLE
            binding.btnAns2Math.visibility = View.VISIBLE
            binding.btnAns3Math.visibility = View.VISIBLE
            binding.btnAns4Math.visibility = View.VISIBLE
            binding.btnImgRulesMath.visibility = View.VISIBLE

            binding.lottieAnimLoading.visibility = View.GONE

        }
    }
}