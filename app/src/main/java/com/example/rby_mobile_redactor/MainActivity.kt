package com.example.rby_mobile_redactor

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view -> run {
            AlertDialog.Builder(this).run {
                setTitle("Добавление ячейки")
                setPositiveButton("Текст") { _, _ -> {}}
                setNegativeButton("Картинка") { _, _ -> {}}
            }.create().show()
                Snackbar.make(view, "Элемент добавлен (нет)", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }
    }
}
