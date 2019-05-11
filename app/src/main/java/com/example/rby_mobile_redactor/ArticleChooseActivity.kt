package com.example.rby_mobile_redactor

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.widget.ArrayAdapter
import java.io.File

class ArticleChooseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_choose)

        supportActionBar!!.displayOptions =
            ActionBar.DISPLAY_SHOW_HOME or ActionBar.DISPLAY_SHOW_TITLE or ActionBar.DISPLAY_HOME_AS_UP or ActionBar.DISPLAY_USE_LOGO
        actionBar!!.setIcon(R.drawable.ic_r)


    }

    private val articlesPath = "articles.json"

    fun loadArticles() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayOf(""))
        if (File(articlesPath).exists()) {
            openFileInput(articlesPath).use { input ->

            }
        }

    }
}
