package com.chocomiruku.homework7

import android.content.Context
import android.content.res.AssetManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val KEY_PARSED_LIST = "key_parsed_list"
const val LIST_PARSED = "list_parsed"

fun AssetManager.readFile(fileName: String) = open(fileName)
    .bufferedReader()
    .use { it.readText() }

fun parseJson(context: Context) : List<Model> {
    val jsonString = context.assets.readFile("data.json")
    val typeToken = object : TypeToken<List<Model>>() {}.type
    return Gson().fromJson(jsonString, typeToken)
}