package com.example.android_sdk_example.services.wallet_proxy

import com.concordium.sdk.responses.blocksummary.updates.queues.AnonymityRevokerInfo
import com.concordium.sdk.responses.blocksummary.updates.queues.IdentityProviderInfo
import retrofit2.Call
import retrofit2.http.*
import java.io.Serializable

internal interface ProxyBackend {
    @GET("v1/ip_info")
    fun getIdentityProviderInfo(): Call<ArrayList<IdentityProvider>>
}

data class SubmissionData(
    val submissionId: String
)

data class IdentityProvider(
    val ipInfo: IdentityProviderInfo,
    val arsInfos: Map<String, AnonymityRevokerInfo>,
    val metadata: IdentityProviderMetaData
) : Serializable

data class IdentityProviderMetaData(
    val icon: String,
    val issuanceStart: String,
    val support: String?,
    val recoveryStart: String?
) : Serializable