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
        val req = Request.Builder().url("https://jsonplaceholder.typicode.com/todos/1").build()
        client.newCall(req).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                binding.tvP.text = "connection error"
            }

            override fun onResponse(call: Call, response: Response) {
                val j = response.body!!.string()
                val jo = JSONObject(j)

                val mytodo = DataModel(
                    jo.getInt("userId"),
                    jo.getInt("id"),
                    jo.getString("title"),
                    jo.getString("completed")
                )
runOnUiThread {

    binding.tvP.text = mytodo.task }
            }

        })
    }
}