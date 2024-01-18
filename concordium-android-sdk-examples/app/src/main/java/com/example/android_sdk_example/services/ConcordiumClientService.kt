package com.example.android_sdk_example.services

import com.concordium.sdk.ClientV2
import com.concordium.sdk.Connection
import com.concordium.sdk.TLSConfig
import com.example.android_sdk_example.Constants

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