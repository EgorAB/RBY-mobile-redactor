package com.example.rby_mobile_redactor

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        start_button?.setOnClickListener {
            startActivity(Intent(this, ArticleChooseActivity::class.java))
        }
    }
}
