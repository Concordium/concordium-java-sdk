package com.example.android_sdk_example.services

import com.example.android_sdk_example.Constants
import com.example.android_sdk_example.services.wallet_proxy.IdentityProvider
import com.example.android_sdk_example.services.wallet_proxy.ProxyBackendConfig

object ConcordiumWalletProxyService {
    private val backend = ProxyBackendConfig(Constants.WALLET_PROXY_URL).backend

    fun getIdentityProviderInfo(): ArrayList<IdentityProvider> {
        val response = backend.getIdentityProviderInfo().execute()
        if (response.isSuccessful) {
            response.body()?.let { return it }
        }
        throw Exception(response.message())
    }
}
