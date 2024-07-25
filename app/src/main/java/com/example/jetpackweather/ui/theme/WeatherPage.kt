package com.example.jetpackweather.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.jetpackweather.WeatherViewModel
import com.example.jetpackweather.retrofit.NetworkResponse
import com.example.jetpackweather.retrofit.model.WeatherDataModel

@Composable
fun WeatherPage(viewModel: WeatherViewModel) {

    var cityName by remember {
        mutableStateOf("")
    }

    val weatherResult =viewModel.weatherResult.observeAsState()

    //hide keyboard after getting result
    val keyboardController= LocalSoftwareKeyboardController.current

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp
    val density = configuration.densityDpi

    val cameraMargin = if (screenHeight > screenWidth) {
        // Calculate margin for camera cutout in portrait mode
        (screenWidth * 0.1).dp // 10% of screen width
    } else {
        // Calculate margin for camera cutout in landscape mode
        (screenHeight * 0.1).dp // 10% of screen height
    }

    Box(
        modifier = Modifier
            .padding(top = cameraMargin)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = cityName,
                    onValueChange = {
                    cityName = it
                },
                    label = {
                        Text(text = "Search for any location")
                    })
                IconButton(onClick = {
                    viewModel.getData(cityName)
                    keyboardController?.hide()
                }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
            }

           when(val result = weatherResult.value){
               is NetworkResponse.Error -> {
                   Text(text = result.message)
               }
               NetworkResponse.Loading -> {
                   CircularProgressIndicator()
               }
               is NetworkResponse.Success -> {
                   WeatherDetails(data = result.data)
               }
               null -> {

               }
           }


        }
    }


}

@Composable
fun WeatherDetails(data:WeatherDataModel){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location",
                modifier = Modifier.size(40.dp))

            Text(
                text = data.location.name+", ",
                fontSize = 30.sp
                )
            Text(
                text = data.location.country,
                fontSize = 18.sp,
                color = Color.Gray
            )

        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "${data.current.temp_c} ° c",
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center)
        AsyncImage(
            modifier = Modifier.size(160.dp),
            model = "https:${data.current.condition.icon}".replace("64x64","128x128"),
            contentDescription = "Condition Icon" )
        Text(
            text = data.current.condition.text,
            fontSize = 20.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Card {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    WeatherCardValue(key = "Humidity", value =data.current.humidity )
                    WeatherCardValue(key = "Feels like", value =data.current.feelslike_c )

                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    WeatherCardValue(key = "UV", value =data.current.uv )
                    WeatherCardValue(key = "Heat Index", value =data.current.heatindex_c )

                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    WeatherCardValue(key = "Local time", value =data.location.localtime.split(" ")[1])
                    WeatherCardValue(key = "Local date", value =data.location.localtime.split(" ")[0])

                }

            }
        }

    }
    
}

@Composable
fun WeatherCardValue(key:String,value:String){
    Column(
        modifier = Modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = key,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray)
    }
}