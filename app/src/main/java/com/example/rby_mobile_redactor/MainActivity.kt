package com.example.rby_mobile_redactor

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.graphics.drawable.ColorDrawable
import android.widget.ArrayAdapter
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            run {
                AlertDialog.Builder(this).run {
                    setTitle("Добавление ячейки")
                    setPositiveButton("Текст") { _, _ -> {} }
                    setNegativeButton("Картинка") { _, _ -> {} }
                }.create().show()
//                Snackbar.make(view, "Элемент добавлен (нет)", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
            }
        }
        left_drawer.adapter = ArrayAdapter(this, R.layout.drawer_list_item,
            arrayOf("Sample1", "Sample2", "Sample3", "Sample 4"))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.confirm_file_creation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.send -> {
            }
//            R.id.app_bar_search -> {
//            }
        }
        return super.onOptionsItemSelected(item)
    }
}
