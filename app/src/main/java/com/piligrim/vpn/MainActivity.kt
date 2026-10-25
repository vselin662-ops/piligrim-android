package com.piligrim.vpn
import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    private val orchestratorUrl = "http://37.18.102.64:8000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tvStatus = findViewById<TextView>(R.id.tvStatus)
        val btnConnect = findViewById<Button>(R.id.btnConnect)

        btnConnect.setOnClickListener {
            tvStatus.text = "Запрос полиморфной стратегии..."
            getStrategy()
        }
    }

    private fun getStrategy() {
        val request = Request.Builder().url("$orchestratorUrl/strategy?client_id=Selin_888&operator=android").build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { findViewById<TextView>(R.id.tvStatus).text = "Ошибка сети: ${e.message}" }
            }
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                runOnUiThread {
                    if (response.isSuccessful && body != null) {
                        findViewById<TextView>(R.id.tvStatus).text = "✅ СТРАТЕГИЯ ПОЛУЧЕНА:\n\n$body"
                        Toast.makeText(this@MainActivity, "Успех!", Toast.LENGTH_SHORT).show()
                    } else {
                        findViewById<TextView>(R.id.tvStatus).text = "Ошибка сервера: ${response.code}"
                    }
                }
            }
        })
    }
}
