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
import com.quizapp.quizfoirtopics.quizentity.MyInterceptor
import com.quizapp.quizfoirtopics.quizentity.QuizServiceAPI
import com.quizapp.quizfoirtopics.utilitas.Constances
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MusicFragment : Fragment() {
    private var _binding: FragmentMusicBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("FragmentStartBinding = null")

    private val listImagesLogoMusic = listOf(
        R.drawable.invitation,
        R.drawable.studiomixer,
        R.drawable.musicalinstrument,
        R.drawable.saxophone,
        R.drawable.music_2,
        R.drawable.music_1,
        R.drawable.listen,
        R.drawable.musicnotes,
        R.drawable.guitar,
    )

    private var currentQuestion = ""
    private var currentAns = ""

    private val loading = 1
    private val correct = 2
    private val fail = 3

    private val listFakeAnsMusic = listOf(
        "composer ",
        "songwriter",
        "conductor",
        "performer",
        "musician ",
        "band",
        "orchestra",
        "soloist",
        "singer",
        "lead singer",
        "backing group ",
        "drummer",
        "drum kit",
        "guitarist ",
        "lead guitar",
        "bass guitar",
        "acoustic guitar",
        "amplifier",
        "microphone",
        "loudspeaker ",
        "keyboard player",
        "synthesizer",
        "saxophone player",
        "concerto ",
        "symphony ",
        "overture",
        "choir",
        "voi",
        "ear"
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
        _binding = FragmentMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initProgBar(loading)
        binding.btnImgRulesMusic.setOnClickListener {
            findNavController().navigate(R.id.action_musicFragment_to_aboutAppInfoFragment)
        }

        try {
            binding.btnImgExitMusic.setOnClickListener {
                initAlertDialogForExit()
            }
            binding.btnAns1Music.setOnClickListener {
                sendAnsFromUser(binding.btnAns1Music.text.toString())
            }
            binding.btnAns2Music.setOnClickListener {
                sendAnsFromUser(binding.btnAns2Music.text.toString())
            }
            binding.btnAns3Music.setOnClickListener {
                sendAnsFromUser(binding.btnAns3Music.text.toString())
            }
            binding.btnAns4Music.setOnClickListener {
                sendAnsFromUser(binding.btnAns4Music.text.toString())
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
                val result = api.getQuestion(category = "music")
                if (result.isSuccessful) {
                    currentQuestion = result.body()!![0].question!!
                    currentAns = result.body()!![0].answer!!

                    binding.tvQuestionTextMusic.text = currentQuestion
                    binding.btnAns1Music.text = currentAns
                    binding.btnAns2Music.text = listFakeAnsMusic.random()
                    binding.btnAns3Music.text = listFakeAnsMusic.random()
                    binding.btnAns4Music.text = listFakeAnsMusic.random()
                    binding.imgLogoQuestionMusic.setImageResource(listImagesLogoMusic.random())

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
            binding.tvTitleQuizMusic.visibility = View.GONE
            binding.btnImgExitMusic.visibility = View.GONE
            binding.imgLogoQuestionMusic.visibility = View.GONE
            binding.tvQuestionTextMusic.visibility = View.GONE
            binding.tvDovnText.visibility = View.GONE
            binding.btnAns1Music.visibility = View.GONE
            binding.btnAns2Music.visibility = View.GONE
            binding.btnAns3Music.visibility = View.GONE
            binding.btnAns4Music.visibility = View.GONE
            binding.btnImgRulesMusic.visibility = View.GONE

            when (kindOfAnim) {
                loading -> {
                    binding.lottieAnimLoading.visibility = View.VISIBLE
                }
                correct -> {
                    MusicFragmentDirections.actionMusicFragmentToCorrectAnsFragment(typeQuiz = Constances.TYPE_QUIZ_GEO)
                        .also {
                            findNavController().navigate(it)
                        }
                }
                fail -> {
                    MusicFragmentDirections.actionMusicFragmentToVrongAnsFragment(typeQuizz = Constances.TYPE_QUIZ_GEO)
                        .also {
                            findNavController().navigate(it)
                        }
                }
            }

            loadList()

            delay(1100)

            binding.tvTitleQuizMusic.visibility = View.VISIBLE
            binding.btnImgExitMusic.visibility = View.VISIBLE
            binding.imgLogoQuestionMusic.visibility = View.VISIBLE
            binding.tvQuestionTextMusic.visibility = View.VISIBLE
            binding.tvDovnText.visibility = View.VISIBLE
            binding.btnAns1Music.visibility = View.VISIBLE
            binding.btnAns2Music.visibility = View.VISIBLE
            binding.btnAns3Music.visibility = View.VISIBLE
            binding.btnAns4Music.visibility = View.VISIBLE
            binding.btnImgRulesMusic.visibility = View.VISIBLE

            binding.lottieAnimLoading.visibility = View.GONE

        }
    }
}