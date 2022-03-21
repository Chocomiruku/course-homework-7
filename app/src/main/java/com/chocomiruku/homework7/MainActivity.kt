package com.chocomiruku.homework7


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.chocomiruku.homework7.databinding.ActivityMainBinding
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ModelAdapter
    private var parsedModels = listOf<Model>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        adapter = ModelAdapter()
        binding.parsedModelsList.adapter = adapter

        savedInstanceState?.let {
            adapter.submitList(savedInstanceState.getParcelableArrayList(KEY_PARSED_LIST))
            parsedModels = adapter.currentList
        }

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadCastReceiver, IntentFilter(LIST_PARSED))

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
            binding.progressIndicator.show()
            parseWithIntentService()
        }

        setContentView(binding.root)
    }

    private fun parseWithThread() {
        val backgroundThread = Thread {
            Thread.sleep(5000)
            parsedModels = parseJson(this)
            loadModelsList()
        }
        backgroundThread.start()
    }

    private fun parseWithExecutor() {
        Executors.newSingleThreadExecutor().execute {
            Thread.sleep(5000)
            parsedModels = parseJson(this)
            loadModelsList()
        }
    }

    private fun parseWithIntentService() {
        val intent = Intent(this, MyIntentService::class.java)
        startService(intent)
    }

    private fun loadModelsList() {
        runOnUiThread {
            binding.progressIndicator.hide()
            adapter.submitList(parsedModels)
        }
    }

    // Deprecated
    private val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            when (intent?.action) {
                LIST_PARSED -> {
                    binding.progressIndicator.hide()
                    adapter.submitList(intent.getParcelableArrayListExtra(KEY_PARSED_LIST))
                    parsedModels = adapter.currentList
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(KEY_PARSED_LIST, ArrayList<Parcelable>(parsedModels))
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(broadCastReceiver)
    }
}