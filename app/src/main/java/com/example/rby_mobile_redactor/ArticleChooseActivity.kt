package com.example.rby_mobile_redactor

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
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
            val newLogin = logInLayout.findViewById<EditText>(R.id.login).text.toString()
            val password = logInLayout.findViewById<EditText>(R.id.password).text.toString()
            if (newLogin != "" && password != "") {
                logIn(newLogin, password)
                login = newLogin
            } else {
                Toast.makeText(this, "Введите логин и пароль", Toast.LENGTH_LONG).show()
            }
        }

        fillTitles()

        newArticle.setOnClickListener {
            editArticle(null)
        }
    }

    private var login = ""

    private val articlesPath = "/storage/Android/data/com.example.rby_mobile_redactor/saves"
    private val address = "http://172.20.20.103:8080"

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
                    upload(queue)
                    updateTitles()
                    titleLayout.removeAllViews()
                    titleLayout.addView(LayoutInflater.from(this).inflate(R.layout.account, null))
                }
            },
            { error ->
                Log.d("ERROR", error.toString())
            }
        ))
    }

    private fun updateTitles() {
        assert(login != "")
        val queue = Volley.newRequestQueue(this)
        queue.add(JsonObjectRequest(
            Request.Method.POST,
            address,
            JSONObject(
                mapOf(
                    "purpose" to "get_user",
                    "content" to JSONObject(
                        mapOf(
                            "login" to login
                        )
                    )
                )
            ),
            { response ->
                Log.d("RESPONSE", response.toString())
                if (response.has(login)) {
                    val articles = response.getJSONObject(login).getJSONObject("articles")
                    val synchronized = ArrayList<String>()
                    for (key in articles.keys()) {
                        File("$articlesPath/$key").outputStream().use { output ->
                            output.write(articles.getJSONObject(key).toString().toByteArray())
                        }
                        synchronized.add(key)
                    }
                    fillTitles(synchronized)
                }
            },
            { error ->
                Log.d("ERROR", error.toString())
            }
        ))
    }

    private fun fillTitles(synchronized: ArrayList<String> = arrayListOf()) {
        if (File(articlesPath).listFiles() != null) {
            for (file in File(articlesPath).listFiles()) {
                openFileInput(articlesPath).use { input ->
                    val article = JSONObject(String(input.readBytes()))
                    val articlePreview =
                        LayoutInflater.from(this).inflate(R.layout.article_preview, null) as LinearLayout
                    articlePreview.findViewById<TextView>(R.id.title).text = article.getString("title").let {
                        if (it != "") it else file.name
                    }
                    articlePreview.findViewById<ImageButton>(R.id.edit).setOnClickListener {
                        editArticle(file.name)
                    }
                    articlePreview.findViewById<ImageButton>(R.id.cloud).run {
                        if (synchronized == null || file.name in synchronized) {
                            markSynchronized()
                        } else {
                            setOnClickListener {
                                if (login == "") {
                                    Toast.makeText(
                                        this@ArticleChooseActivity,
                                        "Чтобы синхронизироваться с облаком, вам необходимо войти",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    upload(Volley.newRequestQueue(this@ArticleChooseActivity), arrayOf(file.name))
                                    updateTitles()
                                }
                            }
                            titleLayout.addView(articlePreview)
                        }
                    }
                }
            }
        }
    }

    private fun ImageButton.markSynchronized() {
        setImageDrawable(this@ArticleChooseActivity.resources.getDrawable(R.drawable.ic_cloud_done_black_24dp))
        isClickable = false
        isFocusable = false
    }

    private fun editArticle(id: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("login", login)
        if (id == null) {
            File("$id.json").createNewFile()
        }
        intent.putExtra("path", "$articlesPath/${id ?: (File(articlesPath).listFiles()?.size ?: 0)}")
        startActivity(intent)
    }

    private fun upload(queue: RequestQueue, exact: Array<String>? = null) {
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
                            login to JSONObject(
                                mapOf(
                                    "articles" to JSONObject(if (exact == null) files.map { file ->
                                        file.name to file.inputStream().use { input -> input.read() }
                                    }.toMap() else Array(exact.size) { i ->
                                        exact[i] to File("$articlesPath/${exact[i]}").inputStream().use { input -> input.read() }
                                    }.toMap())
                                )
                            )
                        )
                    )
                )
            ),
            { response ->
                Log.d("RESPONSE", response.toString())
                if (response.getString("result") != "ok") {
                    AlertDialog.Builder(this).run {
                        setMessage("Загрузка данных на сервер не удалась")
                        setPositiveButton("Попробовать ещё") { dialog, which ->

                        }
                    }
                }
            },
            { error ->
                Log.d("ERROR", error.toString())
            }
        ))
    }
}
