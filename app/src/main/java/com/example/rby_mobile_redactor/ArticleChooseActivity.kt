package com.example.rby_mobile_redactor

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_article_choose.*
import org.json.JSONObject
import java.io.File

class ArticleChooseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_choose)

        val logInLayout = LayoutInflater.from(this).inflate(R.layout.log_in, null)
        titleLayout.addView(logInLayout)
        logInLayout.findViewById<Button>(R.id.logIn).setOnClickListener {
            login = logInLayout.findViewById<EditText>(R.id.login).text.toString()
            val password = logInLayout.findViewById<EditText>(R.id.password).text.toString()
            if (login != "" && password != "") {
                logIn(login, password)
            }
        }

        fillTitles()

        newArticle.setOnClickListener { createArticle() }
    }

    private var login = ""

    private val articlesPath = "articles.json"

    private fun logIn(login: String, password: String) {
        val queue = Volley.newRequestQueue(this)
        queue.add(JsonObjectRequest(
            Request.Method.POST,
            "172.20.20.158:80",
            JSONObject(mapOf(
                "purpose" to "create_user",
                "content" to JSONObject(mapOf(
                    "login" to login,
                    "password" to password
                ))
            )),
            { response ->
                Log.d("RESPONSE", response.toString())
                //if (response.getString("u_r_created") == "ok")
            },
            { error ->
                Log.d("ERROR", error.toString())
            }
        ))
    }

    private fun fillTitles() {
        if (File(articlesPath).exists()) {
            openFileInput(articlesPath).use { input ->
                val titles = JSONObject(String(input.readBytes()))
                for (i in 0 until titles.length()) {
                    val articlePreview =
                        LayoutInflater.from(this).inflate(R.layout.article_preview, null) as LinearLayout
                    articlePreview.findViewById<TextView>(R.id.title).text = titles.getString("title")
                    titleLayout.addView(articlePreview)
                }
            }
        }
    }

    private fun createArticle() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}
