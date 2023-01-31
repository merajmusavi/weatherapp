package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.ActivityMainBinding
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.sql.Date
import java.sql.Time
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val client = OkHttpClient()

        val req = Request.Builder()
            .url("https://api.openweathermap.org/data/2.5/weather?q=manchester&appid=0ae333a163d630d5372038e78ad370dc&unit=metric")
            .build()
        client.newCall(req).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                val j = response.body!!.string()
                val jo = JSONObject(j)

                val weatherArray = jo.getJSONArray("weather")
                val obj_weather = weatherArray.getJSONObject(0)
                val icon_id = obj_weather.getString("icon")
                val sunrise = jo.getJSONObject("sys").getInt("sunrise")
                val sunset = jo.getJSONObject("sys").getInt("sunset")
                val url_icon = "https://openweathermap.org/img/wn/${icon_id}@2x.png"
                val StatusDescription = obj_weather.getString("description")
                val weather_detail = jo.getJSONObject("main")
                val temp_min = weather_detail.getDouble("temp_min")
                val temp_max = weather_detail.getDouble("temp_min")
                val feels_like = weather_detail.getDouble("feels_like")
                val pressure = weather_detail.getInt("pressure")


                runOnUiThread {
                    ShowName(
                        jo.getString("name"),
                        StatusDescription,
                        url_icon,
                        sunrise,
                        sunset,
                        feels_like,
                        temp_min, temp_max, pressure
                    )
                }
            }

        })
    }

    fun ShowName(
        cityName: String,
        description: String,
        imageUrl: String,
        sunrise: Int,
        sunset: Int,
        feels_like: Double,
        temp_min: Double,
        temp_max: Double,
        pressure: Int
    ) {
        binding.tvCityName.text = cityName
        binding.description.text = description
        binding.tvSunrise.text = "sunrise : " + showTimeBasedOnUnixTime(sunrise).toString()
        binding.tvSunset.text = "sunset : " + showTimeBasedOnUnixTime(sunset).toString()
        binding.tvFeelLike.text = feels_like.toString()
        binding.tvPressure.text = pressure.toString()
        binding.tvTempMax.text = temp_max.toString()
        binding.tvTempMin.text = temp_min.toString()
        Glide.with(this).load(imageUrl).into(binding.ivWeather)
    }

    fun showTimeBasedOnUnixTime(unixtime: Int): String {
        val time = unixtime * 1000.toLong()
        var time_converter = Date(time)

        val formater = SimpleDateFormat("HH:MM a")
        return formater.format(time_converter)


    }
}