package com.example.android_sdk_example

import android.content.Context
import android.content.SharedPreferences
import androidx.activity.ComponentActivity
import cash.z.ecc.android.bip39.Mnemonics
import cash.z.ecc.android.bip39.toSeed
import com.concordium.sdk.crypto.wallet.ConcordiumHdWallet

/***
 * Class to hide storage details from the rest of the project.
 */
class Storage(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences("EXAMPLE", ComponentActivity.MODE_PRIVATE)
    val seedPhrase = StorageAccessor(sharedPreferences, "seed_phrase")
    val identityProviderIndex = StorageAccessor(sharedPreferences, "provider_index")
    val identityUrl = StorageAccessor(sharedPreferences, "identity_url")
    val accountAddress = StorageAccessor(sharedPreferences, "account_address")
    val identity = StorageAccessor(sharedPreferences, "identity")

    @OptIn(ExperimentalStdlibApi::class)
    fun getWallet(): ConcordiumHdWallet {
        val seedPhrase = this.seedPhrase.get()
        val seedAsHex = Mnemonics.MnemonicCode(seedPhrase!!.toCharArray()).toSeed().toHexString()
        return ConcordiumHdWallet.fromHex(seedAsHex, Constants.NETWORK)
    }
}

open class StorageAccessor(
    private val sharedPreferences: SharedPreferences,
    private val key: String
) {
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