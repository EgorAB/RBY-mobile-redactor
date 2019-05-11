package com.example.rby_mobile_redactor

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_article_choose.*
import org.json.JSONArray
import java.io.File

class ArticleChooseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_choose)

        supportActionBar!!.displayOptions =
            ActionBar.DISPLAY_SHOW_HOME or ActionBar.DISPLAY_SHOW_TITLE or ActionBar.DISPLAY_HOME_AS_UP or ActionBar.DISPLAY_USE_LOGO
        actionBar!!.setIcon(R.drawable.ic_r)

        titleLayout.setOnClickListener { createArticle() }

        fillTitles()
    }

    private val articlesPath = "articles.json"

    private fun fillTitles() {
        if (File(articlesPath).exists()) {
            openFileInput(articlesPath).use { input ->
                val titles = JSONArray(String(input.readBytes()))
                for (i in 0 until titles.length()) {
                    val articlePreview = LayoutInflater.from(this).inflate(R.layout.article_preview, null) as LinearLayout
                    articlePreview.findViewById<TextView>(R.id.title).text = titles.getString(i)
                    titleLayout.addView(articlePreview)
                }
            }
        }
    }

    private fun createArticle() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}
