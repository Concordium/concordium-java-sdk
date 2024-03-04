package com.concordium.example.wallet

import android.content.Context
import android.content.SharedPreferences
import androidx.activity.ComponentActivity
import com.concordium.sdk.crypto.wallet.ConcordiumHdWallet

/***
 * Class to hide storage details from the rest of the project.
 */
class Storage(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences("EXAMPLE", ComponentActivity.MODE_PRIVATE)
    /**
     * @see seedPhrase the seed phrase should not be stored like this in shared preferences as it not secure. We do it here for
    demonstration purposes only
     */
    val seedPhrase = StorageAccessor(sharedPreferences, "seed_phrase")
    val identityProviderIndex = StorageAccessor(sharedPreferences, "provider_index")
    val identityUrl = StorageAccessor(sharedPreferences, "identity_url")
    val accountAddress = StorageAccessor(sharedPreferences, "account_address")
    val identity = StorageAccessor(sharedPreferences, "identity")

    fun getWallet(): ConcordiumHdWallet {
        val seedPhrase = this.seedPhrase.get()
        return ConcordiumHdWallet.fromSeedPhrase(seedPhrase, Constants.NETWORK)
    }
}

open class StorageAccessor(
    private val sharedPreferences: SharedPreferences,
    private val key: String
) {
    private var cache: String? = null

    fun get(): String? {
        cache = cache ?: this.sharedPreferences.getString(key, null)
        return cache
    }

    fun set(value: String) {
        this.sharedPreferences.edit().putString(key, value).apply()
        cache = value
    }
}
