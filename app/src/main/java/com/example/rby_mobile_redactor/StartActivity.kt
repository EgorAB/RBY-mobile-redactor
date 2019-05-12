package com.example.rby_mobile_redactor

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import android.support.v7.widget.Toolbar
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.PersistableBundle
import android.support.design.widget.AppBarLayout
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_start.*
import java.io.InputStream

class StartActivity : AppCompatActivity() {
    val pickImage:Int = 108
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
            start_button?.setOnClickListener { view->
                startActivity(Intent(this, ArticleChooseActivity::class.java))
            }
        }
}
