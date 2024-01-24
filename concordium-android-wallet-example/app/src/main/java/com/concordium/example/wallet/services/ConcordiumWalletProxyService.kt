package com.concordium.example.wallet.services

import com.concordium.example.wallet.Constants
import com.concordium.example.wallet.services.wallet_proxy.IdentityProvider
import com.concordium.example.wallet.services.wallet_proxy.ProxyBackendConfig

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
