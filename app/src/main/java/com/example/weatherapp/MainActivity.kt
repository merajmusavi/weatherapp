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
            .url("https://api.openweathermap.org/data/2.5/weather?q=tehran&appid=0ae333a163d630d5372038e78ad370dc")
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

                runOnUiThread {
                    ShowName(jo.getString("name"), StatusDescription,url_icon,sunrise,sunset)
                } 
            }

        })
    }

    fun ShowName(cityName: String, description: String,imageUrl:String,sunrise:Int,sunset:Int) {
        binding.tvCityName.text = cityName
        binding.description.text = description
        binding.tvSunrise.text = showTimeBasedOnUnixTime(sunrise).toString()
        binding.tvSunset.text = showTimeBasedOnUnixTime(sunset).toString()
        Glide.with(this).load(imageUrl).into(binding.ivWeather)
    }
    fun showTimeBasedOnUnixTime(unixtime:Int):String{
        val time = unixtime*1000.toLong()
        var time_converter = Date(time)

        val formater = SimpleDateFormat("HH:MM a")
        return formater.format(time_converter)


    }
}