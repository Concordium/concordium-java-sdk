package com.example.android_sdk_example.wallet_proxy

import com.concordium.sdk.responses.blocksummary.updates.queues.Description
import java.io.Serializable
import retrofit2.Call
import retrofit2.http.*

interface ProxyBackend {
    @PUT("v0/testnetGTUDrop/{accountAddress}")
    fun requestGTUDrop(@Path("accountAddress") accountAddress: String): Call<SubmissionData>

    // Identity Provider
    @GET("v1/ip_info")
    fun getIdentityProviderInfo(): Call<ArrayList<IdentityProvider>>

    @GET
    fun checkIdentityProvider(@Url url: String): Call<String>
}

data class SubmissionData(
    val submissionId: String
)

data class IdentityProvider(
    val ipInfo: IdentityProviderInfo,
    val arsInfos: Map<String, ArsInfo>,
    val metadata: IdentityProviderMetaData
) : Serializable

data class IdentityProviderMetaData(
    val icon: String,
    val issuanceStart: String,
    val support: String?,
    val recoveryStart: String?

) : Serializable {
    fun getSupportWithDefault(): String {
        return support ?: "support@concordium.software"
    }
}

data class IdentityProviderInfo(
    val ipIdentity: Int,
    val ipDescription: Description,
    val ipVerifyKey: String,
    val ipCdiVerifyKey: String
) : Serializable

data class ArsInfo(
    val arIdentity: Int,
    val arPublicKey: String,
    val arDescription: Description
) : Serializable