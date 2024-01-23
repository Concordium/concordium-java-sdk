package com.example.android_sdk_example.services

import com.concordium.sdk.crypto.wallet.identityobject.IdentityObject
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

class IdentityFetcherService {
    private data class IdentityResponse(
        val status: Status,
        val token: IdentityWrapper?,
        val detail: String?,
    ) {
        enum class Status {
            @JsonProperty("done")
            DONE,

            @JsonProperty("pending")
            PENDING,

            @JsonProperty("error")
            ERROR,
        }
    }

    private data class RecoveryErrorResponse(
        val code: Int,
        val message: String
    )

    private data class IdentityWrapper(val identityObject: VersionedIdentity)

    @JsonAutoDetect
    private data class VersionedIdentity(
        val v: Number,
        val value: IdentityObject
    )

    private interface IdentityProviderBackend {
        @GET
        fun getIdentity(@Url url: String): Call<IdentityResponse>

        @GET
        fun recoverIdentity(@Url url: String): Call<VersionedIdentity>
    }

    private fun initializeBackend(): IdentityProviderBackend {
        val retrofit =
            Retrofit.Builder()
                // This baseUrl is never used, as all our endpoints require a replacement url
                .baseUrl("http://dummy.com/")
                .addConverterFactory(JacksonConverterFactory.create(jacksonObjectMapper()))
                .build()
        return retrofit.create(IdentityProviderBackend::class.java)
    }

    /**
     * Get the identity object from an url resulting from identity issuance
     * @return the identity object or null, if the identity is not ready yet
     * @Throws Exception if the identity failed, or the request fails.
     */
    private fun getIdentity(identityUrl: String): IdentityObject? {
        val backend = initializeBackend()
        val response = backend.getIdentity(identityUrl).execute()
        if (response.isSuccessful) {
            response.body()?.let {
                when (it.status) {
                    IdentityResponse.Status.DONE -> return it.token!!.identityObject.value
                    IdentityResponse.Status.PENDING -> return null
                    else -> throw Exception(it.detail ?: "Unknown error")
                }
            }
        }
        throw Exception(response.message())
    }

    /**
     * Loop that calls [getIdentity] until it returns an identity object, or fails
     * @param identityUrl the url that should be the result of the identity issuance protocol
     * @param retryDelay how long the delay between attempts should be, in milliseconds
     * @return the identity that becomes available at the url
     * @throws Exception if the url does not conform to the identity issuance protocol or the identity issuance failed
     */
    suspend fun fetch(identityUrl: String, retryDelay: Long = 10000): IdentityObject {
        while (true) {
            val response = getIdentity(identityUrl)
            if (response != null) {
                return response
            }
            // Try again after retryDelay
            delay(retryDelay)
        }
    }

    /**
     * Get the identity object from a recovery url
     * @param recoveryUrl the url that should be the result of the identity recovery protocol
     * @return the identity that is available at the url
     * @throws Exception if the url does not conform to the identity recovery protocol or the recovery failed
     */
    fun getFromRecovery(recoveryUrl: String): IdentityObject {
        val backend = initializeBackend()
        val response = backend.recoverIdentity(recoveryUrl).execute()
        if (response.isSuccessful) {
            response.body()?.let { return it.value }
        } else {
            response.errorBody()?.let {
                val raw = it.string()
                val errorResponse =
                    jacksonObjectMapper().readValue(raw, RecoveryErrorResponse::class.java)
                throw Exception(errorResponse.message)
            }
        }
        throw Exception(response.message())
    }
}