package com.example.rby_mobile_redactor

import org.json.JSONObject

class Article(val title: String) {

    companion object {

        fun parseJson(json: JSONObject): Article {
            return Article(json.getString("title"))
        }
    }
}