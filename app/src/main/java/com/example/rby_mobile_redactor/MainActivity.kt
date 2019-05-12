package com.example.rby_mobile_redactor

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.AppBarLayout
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.SpannableStringBuilder
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import java.io.InputStream
import kotlinx.android.synthetic.main.activity_start.view.*

class MainActivity : AppCompatActivity() {
    val pickImage: Int = 108
    private val imageCellArray = arrayListOf<ImageView>()
    private val editTextCellArray = arrayListOf<EditText>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //загрузка
        load(intent.getStringExtra("path"))
        editTextCellArray.add(editText2)
        fab.setOnClickListener { view ->
            run {
                AlertDialog.Builder(this).run {
                    setTitle("Добавление ячейки")
                    setPositiveButton("Текстовый блок") { _, _ ->
                        run {
                            createTextField()
                        }
                    }
                    setNegativeButton("Изображение") { _, _ ->
                        run {
                            val photoPickerIntent = Intent(Intent.ACTION_PICK)
                            photoPickerIntent.type = "image/*"
                            startActivityForResult(photoPickerIntent, pickImage)
                        }
                    }
                }.create().show()
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val toolBar: Toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun load(savePath: String) {
        //вызываем методы createTextField и createImageField, перед этим распарсив файл сохранения
    }

    private fun save(savePath: String) {
        //сохраняем все в жсон

    }

    private fun parseFile(file: JSONArray){

    }

    private fun createTextField(textValue: String = "") {
        val e: EditText = EditText(this)
        e.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        e.hint = "Напишите что-нибудь"
        e.text = SpannableStringBuilder(textValue)
        findViewById<AppBarLayout>(R.id.appBarLayout)?.addView(e)
//        Snackbar.make(view, "Добавлено текстовое поле", Snackbar.LENGTH_LONG)
//            .setAction("Action", null).show()
        editTextCellArray.add(e)
    }

    private fun createImageField(bitmap: Bitmap) {

        val k: Float = appBarLayout.width.toFloat() / bitmap.width
        val i = ImageView(this)
        i.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        i.setImageBitmap(Bitmap.createScaledBitmap(bitmap, appBarLayout.width, (bitmap.height * k).toInt(), true))
        findViewById<AppBarLayout>(R.id.appBarLayout)?.addView(i)
        imageCellArray.add(i)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            pickImage -> if (resultCode == Activity.RESULT_OK) {
                val imageUri: Uri = data?.data!!
                val imageStream: InputStream = contentResolver.openInputStream(imageUri)!!
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                val k: Float = appBarLayout.width.toFloat() / selectedImage.width
                createImageField(selectedImage)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.confirm_file_creation, menu)
        return true
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.delete -> {
                val builder = AlertDialog.Builder(this).run {
                    setTitle("Выберите ячейку для удаления")
                    setPositiveButton("Ок") { _, _ -> {} }
                }
                val dialogDeleteLayout =
                    LayoutInflater.from(builder.context).inflate(R.layout.dialog_delete, null) as LinearLayout
                builder.setView(dialogDeleteLayout)
                val delete_cell_list = dialogDeleteLayout.findViewById<ListView>(R.id.delete_cell_list)
                val dialog = builder.create()
                delete_cell_list.onItemClickListener =
                    AdapterView.OnItemClickListener { parent, view, position, id ->
                        AlertDialog.Builder(builder.context).run {
                            setTitle("Вы уверены что хотите удалить ${(view as TextView).text}?")
                            setPositiveButton("Да") { _, _ ->
                                run {
                                    if (position < editTextCellArray.size) {
                                        appBarLayout.removeView(editTextCellArray[position])
                                        editTextCellArray.removeAt(position)
                                    } else {
                                        appBarLayout.removeView(imageCellArray[position - editTextCellArray.size])
                                        imageCellArray.removeAt(position - editTextCellArray.size)
                                    }
                                    dialog.dismiss()
                                }
                            }
                            setNegativeButton("Нет") { _, _ -> {} }
                        }.create().show()
                    }
                delete_cell_list.adapter = ArrayAdapter<String>(
                    builder.context,
                    android.R.layout.simple_list_item_1, refreshDeleteDialog()
                )
                dialog.show()
            }
            R.id.send -> {
                val builder = AlertDialog.Builder(this).run {
                    setTitle("Отправить статью в редакцию?")
                    setPositiveButton("Да") { _, _ ->
                        run {
                        }
                    }
                    setNegativeButton("Нет") { _, _ ->
                        run {
                        }
                    }
                }
                builder.setView(R.layout.dialog_send)
                builder.create().show()
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return super.onOptionsItemSelected(item)
    }


    private fun refreshDeleteDialog(): ArrayList<String> {
        val d_s_l_values = arrayListOf<String>()
        for (i in 1 until editTextCellArray.size + 1) d_s_l_values.add("$i.Текстовый блок")
        for (i in 1 until imageCellArray.size + 1) d_s_l_values.add("$i.Изображение")
        return d_s_l_values
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onPause() {
        save(intent.getStringExtra("path"))
        super.onPause()
    }
}
