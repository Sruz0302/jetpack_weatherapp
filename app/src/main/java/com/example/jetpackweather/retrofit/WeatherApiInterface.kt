package com.example.jetpackweather.retrofit

import com.example.jetpackweather.retrofit.model.WeatherDataModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiInterface {
    @GET("v1/current.json")
    suspend fun getWeather(
        @Query("key") apiKey :String,
        @Query("q") city :String
    ):Response<WeatherDataModel>

}