package com.example.android_sdk_example.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.android_sdk_example.Storage

class RouterActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val storage = Storage(applicationContext)
        val initialActivity: Class<out Activity> =
            if (storage.accountAddress.get() != null)
                AccountActivity::class.java
            else if (storage.identity.get() != null)
                IdentityActivity::class.java
            else if (storage.seedPhrase.get() != null)
                NewIdentityActivity::class.java
            else
                SeedPhraseActivity::class.java

        val intent = Intent(this, initialActivity)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}