package com.example.rby_mobile_redactor

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_article_choose.*
import kotlinx.android.synthetic.main.log_in.*
import org.json.JSONObject
import java.io.File

class ArticleChooseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_choose)

        val logInLayout = LayoutInflater.from(this).inflate(R.layout.log_in, null)
        titleLayout.addView(logInLayout)
        logIn.setOnClickListener {
            if (loginField.text.toString() != "" && password.text.toString() != "") {
            }
        }

        fillTitles()

        newArticle.setOnClickListener { createArticle() }
    }

    private var login = false

    private val articlesPath = "articles.json"

    private fun logIn(login: String, password: String) {

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
