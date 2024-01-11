package com.example.android_sdk_example

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.lang.reflect.Type

/***
 * Class to hide storage details from the rest of the project.
 */
class Storage(sharedPreferences: SharedPreferences) {
    val seedPhrase = StorageAccessor(sharedPreferences, "seed_phrase")
    val identityProviderIndex = StorageAccessor(sharedPreferences,"provider_index")
    val identityUrl = StorageAccessor(sharedPreferences,"identity_url")
    val accountAddress = StorageAccessor(sharedPreferences,"account_address")
    val identity = StorageAccessor(sharedPreferences, "identity")
}

open class StorageAccessor(val sharedPreferences: SharedPreferences, val key: String) {
    private var cache: String? = null

    open fun get(): String? {
        cache = cache ?: this.sharedPreferences.getString(key, "")
        return cache
    }

    fun set(value: String) {
        this.sharedPreferences.edit().putString(key, value).apply()
        cache = value
    }

}