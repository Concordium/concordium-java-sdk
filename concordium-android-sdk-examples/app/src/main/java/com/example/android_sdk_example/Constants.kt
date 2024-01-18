package com.example.android_sdk_example

import com.concordium.sdk.crypto.wallet.Network

object Constants {
    // These indices are set to 0, because this wallet only does 1 identity/account, but in a
    // proper wallet with multiple identities/account, these would be actual variables.
    const val IDENTITY_INDEX = 0
    const val CREDENTIAL_COUNTER = 0

    const val AR_THRESHOLD = 2L

    val NETWORK = Network.TESTNET
    const val GRPC_URL = "grpc.testnet.concordium.com"
    const val GRPC_PORT = 20000
    const val CALLBACK_URL = "concordiumwallet-example://identity-issuer/callback"
    const val WALLET_PROXY_URL = "https://wallet-proxy.testnet.concordium.com"
}
