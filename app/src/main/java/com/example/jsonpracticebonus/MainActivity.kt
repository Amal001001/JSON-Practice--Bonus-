package com.example.jsonpracticebonus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.URL

class MainActivity : AppCompatActivity() {
    lateinit var et:EditText
    lateinit var button: Button
    lateinit var tv:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        et = findViewById(R.id.et)
        button = findViewById(R.id.button)
        tv = findViewById(R.id.tv)

        button.setOnClickListener { requestAPI() }

    }

    private fun requestAPI() {
        CoroutineScope(IO).launch {
            val data = async { getNames() }.await()
            if (data.isNotEmpty()) {
                showNames(data)
            }
        }
    }

    private fun getNames(): String {
        var response = ""
        try {
            response = URL("https://dojo-recipes.herokuapp.com/contacts/?format=json").readText(Charsets.UTF_8)
        } catch (e: Exception) {
            println("ISSUE: $e")
        }
        return response
    }

    private suspend fun showNames(data: String) {
        withContext(Main) {
            val jsonArr = JSONArray(data)
                val name = jsonArr.getJSONObject(et.text.toString().toInt()-1).getString("name")
                tv.text = name.toString()
        }
    }

}