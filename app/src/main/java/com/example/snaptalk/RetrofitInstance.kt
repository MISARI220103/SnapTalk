package com.example.snaptalk

import android.provider.SyncStateContract
import com.example.snaptalk.Constants.Constants
import com.example.snaptalk.`interface`.NotificationApi
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object{
        private val retorfit by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val api by lazy {
            retorfit.create(NotificationApi::class.java)
        }
    }
}