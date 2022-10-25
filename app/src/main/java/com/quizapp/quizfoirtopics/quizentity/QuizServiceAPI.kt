package com.quizapp.quizfoirtopics.quizentity

import com.quizapp.quizfoirtopics.BuildConfig
import okhttp3.Interceptor
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface QuizServiceAPI {

    companion object {
        const val BASE_URL = "https://api.api-ninjas.com/v1/"
        const val API_KEY = BuildConfig.API_KEY
        const val TEXT_QUERRY = "rome"
    }


    @GET("trivia")
    suspend fun getQuestion(
        @Query("category") category: String = "geography",
        ): Response<QuizResponse>

}

class MyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("X-Api-Key", QuizServiceAPI.API_KEY)
            .build()
        return chain.proceed(request)
    }
}