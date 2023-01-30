package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.weatherapp.databinding.ActivityMainBinding
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val client = OkHttpClient()
        val req = Request.Builder().url("https://api.openweathermap.org/data/2.5/weather?q=tehran&appid=0ae333a163d630d5372038e78ad370dc").build()
        client.newCall(req).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                val j = response.body!!.string()
                val jo = JSONObject(j)

runOnUiThread {
ShowName(jo.getString("name"))
}
            }

        })
    }
    fun ShowName(cityName:String){
        binding.tvCityName.text = cityName
    }
}