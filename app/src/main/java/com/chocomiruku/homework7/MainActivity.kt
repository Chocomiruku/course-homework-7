package com.chocomiruku.homework7

import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import com.chocomiruku.homework7.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.concurrent.Executors

const val KEY_PARSED_LIST = "key_parsed_list"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ModelAdapter
    private var parsedModels = mutableListOf<Model>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)

        adapter = ModelAdapter()
        binding.parsedModelsList.adapter = adapter

        savedInstanceState?.let {
            adapter.submitList(savedInstanceState.getParcelableArrayList(KEY_PARSED_LIST))
            parsedModels = adapter.currentList
        }

        binding.threadBtn.setOnClickListener {
            if (adapter.currentList.isNotEmpty()) {
                adapter.submitList(emptyList())
            }
            binding.progressIndicator.show()
            parseWithThread()
        }

        binding.executorBtn.setOnClickListener {
            if (adapter.currentList.isNotEmpty()) {
                adapter.submitList(emptyList())
            }
            binding.progressIndicator.show()
            parseWithExecutor()
        }

        binding.intentServiceBtn.setOnClickListener {
            if (adapter.currentList.isNotEmpty()) {
                adapter.submitList(emptyList())
            }
            parseWithIntentService()
        }

        setContentView(binding.root)
    }

    private fun parseWithThread() {
        val backgroundThread = Thread {
            Thread.sleep(5000)
            parseJson()
        }
        backgroundThread.start()
    }

    private fun parseWithExecutor() {
        Executors.newSingleThreadExecutor().execute {
            Thread.sleep(5000)
            parseJson()
        }
    }

    private fun parseWithIntentService() {
        TODO("Not yet implemented")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(KEY_PARSED_LIST, ArrayList<Parcelable>(parsedModels))
    }

    private fun parseJson() {
        val jsonString = assets.readFile("data.json")
        val typeToken = object : TypeToken<List<Model>>() {}.type
        parsedModels = Gson().fromJson(jsonString, typeToken)

        runOnUiThread {
            binding.progressIndicator.hide()
            adapter.submitList(parsedModels)
        }
    }
}