package com.example.android_sdk_example

import com.concordium.sdk.ClientV2
import com.concordium.sdk.Connection
import com.concordium.sdk.TLSConfig

object ConcordiumClientService {
    private val connection = Connection.newBuilder()
        .host("grpc.testnet.concordium.com")
        .port(20000)
        .useTLS(TLSConfig.auto())
        .build()
    private val client = ClientV2.from(connection)

    fun getClient(): ClientV2 {
        return client
    }
}