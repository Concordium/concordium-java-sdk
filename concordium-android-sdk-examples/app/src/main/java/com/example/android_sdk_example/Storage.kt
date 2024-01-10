package com.example.android_sdk_example

import android.content.SharedPreferences

/***
 * Class to hide storage details from the rest of the project.
 */
class Storage(private val sharedPreferences: SharedPreferences) {
    val seedPhrase = StorageAccessor(sharedPreferences, "seed_phrase")
    val identityProviderIndex = StorageAccessor(sharedPreferences,"provider_index")
    val identityUrl = StorageAccessor(sharedPreferences,"identity_url")
    val accountAddress = StorageAccessor(sharedPreferences,"account_address")
}

class StorageAccessor(val sharedPreferences: SharedPreferences, val key: String) {
    var cache: String? = null

    fun get(): String? {
        cache = cache ?: this.sharedPreferences.getString(key, "")
        return cache
    }

    fun set(value: String) {
        this.sharedPreferences.edit().putString(key, value).apply()
        cache = value
    }
}
