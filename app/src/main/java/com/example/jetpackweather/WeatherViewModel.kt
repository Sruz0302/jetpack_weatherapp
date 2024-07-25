package com.example.jetpackweather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackweather.retrofit.Constant
import com.example.jetpackweather.retrofit.NetworkResponse
import com.example.jetpackweather.retrofit.RetrofitInstance
import com.example.jetpackweather.retrofit.model.WeatherDataModel
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApiInterface
    private val _weatherResult =MutableLiveData<NetworkResponse<WeatherDataModel>>()
    val weatherResult :LiveData<NetworkResponse<WeatherDataModel>> = _weatherResult


    fun getData(city: String) {

        viewModelScope.launch {
            _weatherResult.value = NetworkResponse.Loading
            try {
                val response = weatherApi.getWeather(Constant.apikey, city)
                if (response.isSuccessful){
                    response.body()?.let {
                        _weatherResult.value=NetworkResponse.Success(it)
                    }
                    Log.i("Response : ",response.body().toString())

                }else{
                    _weatherResult.value=NetworkResponse.Error("Failed to Load Data")
                    Log.i("Response : ",response.message())

                }
            }catch (e:Exception){
                _weatherResult.value=NetworkResponse.Error("Failed to Load Data")
                Log.i("Response : ","Exception")

            }

        }

    }
}