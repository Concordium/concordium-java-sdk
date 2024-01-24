package com.concordium.example.wallet.services

import com.concordium.sdk.ClientV2
import com.concordium.sdk.Connection
import com.concordium.sdk.TLSConfig
import com.concordium.example.wallet.Constants

object ConcordiumClientService {
    private val connection = Connection.newBuilder()
        .host(Constants.GRPC_URL)
        .port(Constants.GRPC_PORT)
        .useTLS(TLSConfig.auto())
        .build()
    private val client = ClientV2.from(connection)

    fun getClient(): ClientV2 {
        return client
    }
}
