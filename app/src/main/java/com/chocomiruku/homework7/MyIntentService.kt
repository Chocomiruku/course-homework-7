package com.chocomiruku.homework7

import android.app.IntentService
import android.content.Intent
import android.os.Parcelable
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class MyIntentService(name: String = "MyIntentService") : IntentService(name) {
    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            Thread.sleep(5000)
            val parsedModels = parseJson(this)

            val broadcastIntent = Intent(LIST_PARSED)
            broadcastIntent.putParcelableArrayListExtra(KEY_PARSED_LIST, ArrayList<Parcelable>(parsedModels))
            LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
        }
    }
}