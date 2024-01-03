package com.example.android_sdk_example

import android.content.Context
import com.example.android_sdk_example.wallet_proxy.IdentityProvider
import com.example.android_sdk_example.wallet_proxy.ProxyBackendConfig
import retrofit2.Call

class ConcordiumWalletProxyService(context: Context) {
    private val backend = ProxyBackendConfig(context, "https://wallet-proxy.testnet.concordium.com").backend

    fun getIdentityProviderInfo(): ArrayList<IdentityProvider> {
        val response = backend.getIdentityProviderInfo().execute()
        if (response.isSuccessful) {
            response.body()?.let { return it }
        }
        throw Exception(response.message())
    }
}
