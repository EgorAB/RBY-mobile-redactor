package com.example.rby_mobile_redactor

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_article_choose.*
import org.json.JSONObject
import java.io.File

class ArticleChooseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_choose)

        File(articlesPath).mkdir()

        val logInLayout = LayoutInflater.from(this).inflate(R.layout.log_in, null)
        titleLayout.addView(logInLayout)
        logInLayout.findViewById<Button>(R.id.logIn).setOnClickListener {
            login = logInLayout.findViewById<EditText>(R.id.login).text.toString()
            val password = logInLayout.findViewById<EditText>(R.id.password).text.toString()
            if (login != "" && password != "") {
                logIn(login, password)
            } else {
                Toast.makeText(this, "Введите логин и пароль", Toast.LENGTH_LONG).show()
            }
        }

        fillTitles()

        newArticle.setOnClickListener {
            createArticle()
        }
    }

    private var login = ""

    private val articlesPath = "/storage/Android/data/com.example.rby_mobile_redactor/saves"
    private val address = "http://172.20.20.158:8008"

    private fun logIn(login: String, password: String) {
        val queue = Volley.newRequestQueue(this)
        queue.add(JsonObjectRequest(
            Request.Method.POST,
            address,
            JSONObject(
                mapOf(
                    "purpose" to "create_user",
                    "content" to JSONObject(
                        mapOf(
                            "login" to login,
                            "password" to password
                        )
                    )
                )
            ),
            { response ->
                Log.d("RESPONSE", response.toString())
                if (response.getString("u_r_created") == "ok") {
                    uploadAll(queue)
                    updateTitles()
                    titleLayout.removeAllViews()
                    fillTitles()
                }
            },
            { error ->
                Log.d("ERROR", error.toString())
            }
        ))
    }

    private fun updateTitles() {
        val queue = Volley.newRequestQueue(this)
        queue.add(JsonObjectRequest(
            Request.Method.POST,
            "http://172.20.20.158:8008",
            JSONObject(
                mapOf(
                    "purpose" to "get_data",
                    "content" to JSONObject(
                        mapOf(
                            "login" to login
                        )
                    )
                )
            ),
            { response ->
                Log.d("RESPONSE", response.toString())
            },
            { error ->
                Log.d("ERROR", error.toString())
            }
        ))
    }

    private fun fillTitles() {
        for (file in File(articlesPath).listFiles()) {
            openFileInput(articlesPath).use { input ->
                val article = JSONObject(String(input.readBytes()))
                val articlePreview =
                    LayoutInflater.from(this).inflate(R.layout.article_preview, null) as LinearLayout
                articlePreview.findViewById<TextView>(R.id.title).text = article.getString("title")
                titleLayout.addView(articlePreview)
            }
        }
    }

    private fun createArticle() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("login", login)
        intent.putExtra("path", "$articlesPath/${File(articlesPath).listFiles().size}")
        startActivity(intent)
    }

    private fun uploadAll(queue: RequestQueue) {
        assert(login != "")
        val files = File(articlesPath).listFiles()
        queue.add(JsonObjectRequest(
            Request.Method.POST,
            address,
            JSONObject(
                mapOf(
                    "purpose" to "update_articles",
                    "content" to JSONObject(
                        mapOf(
                            login to JSONObject(files.map { file ->
                                file.name to file.inputStream().use { input -> input.read() }
                            }.toMap())
                        )
                    )
                )
            ),
            { response ->
                Log.d("RESPONSE", response.toString())
            },
            { error ->
                Log.d("ERROR", error.toString())
            }
        ))
    }
}
