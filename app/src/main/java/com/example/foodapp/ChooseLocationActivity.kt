package com.example.foodapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.foodapp.databinding.ActivityChooseLocationBinding

class ChooseLocationActivity : AppCompatActivity() {

    private val binding: ActivityChooseLocationBinding by lazy {
        ActivityChooseLocationBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // load thu vi tri len
        val locationList = arrayOf("HCM","Ha Noi","Da Nang","Can Tho")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationList)
        val autoCompleteTextView = binding.listOfLocation
        autoCompleteTextView.setAdapter(adapter)
    }
}
//import android.os.AsyncTask
//import android.os.Bundle
//import android.widget.ArrayAdapter
//import androidx.appcompat.app.AppCompatActivity
//import com.example.foodapp.databinding.ActivityChooseLocationBinding
//import org.json.JSONArray
//import java.io.BufferedReader
//import java.io.InputStreamReader
//import java.net.HttpURLConnection
//import java.net.URL
//
//class ChooseLocationActivity : AppCompatActivity() {
//
//    private val binding: ActivityChooseLocationBinding by lazy {
//        ActivityChooseLocationBinding.inflate(layoutInflater)
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(binding.root)
//
//        // Gọi API và cập nhật AutoCompleteTextView
//        FetchCitiesTask().execute("https://api.example.com/cities") // Thay thế bằng URL API của bạn
//    }
//
//    private inner class FetchCitiesTask : AsyncTask<String, Void, List<String>>() {
//        override fun doInBackground(vararg params: String?): List<String> {
//            val urlString = params[0]
//            val result = mutableListOf<String>()
//            try {
//                val url = URL(urlString)
//                val connection = url.openConnection() as HttpURLConnection
//                connection.requestMethod = "GET"
//                val inputStream = connection.inputStream
//                val reader = BufferedReader(InputStreamReader(inputStream))
//                val response = StringBuilder()
//                var line: String?
//                while (reader.readLine().also { line = it } != null) {
//                    response.append(line)
//                }
//                reader.close()
//                val jsonArray = JSONArray(response.toString())
//                for (i in 0 until jsonArray.length()) {
//                    val city = jsonArray.getJSONObject(i).getString("name")
//                    result.add(city)
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//            return result
//        }
//
//        override fun onPostExecute(result: List<String>?) {
//            super.onPostExecute(result)
//            if (result != null) {
//                val adapter = ArrayAdapter(this@ChooseLocationActivity, android.R.layout.simple_list_item_1, result)
//                val autoCompleteTextView = binding.listOfLocation
//                autoCompleteTextView.setAdapter(adapter)
//            }
//        }
//    }
//}
