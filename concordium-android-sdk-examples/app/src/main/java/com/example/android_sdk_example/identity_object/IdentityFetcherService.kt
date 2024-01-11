package com.example.android_sdk_example.identity_object

import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

class IdentityFetcherService {
    data class IdentityResponse(
        val status: Status,
        val token: IdentityWrapper?,
        val detail: String?,
    ) {
        enum class Status() {
            @SerializedName("done")
            DONE,
            @SerializedName("pending")
            PENDING,
            @SerializedName("error")
            ERROR,
        }
    }

    data class IdentityWrapper(val identityObject: VersionedIdentity)

    data class VersionedIdentity(
        val v: Number,
        val value: IdentityObject
    )

    interface IdentityProviderBackend {
        @GET
        fun getIdentity(@Url url: String): Call<IdentityResponse>
        @GET
        fun recoverIdentity(@Url url: String): Call<VersionedIdentity>
    }

    fun initializeBackend() :IdentityProviderBackend {
        val retrofit =
            Retrofit.Builder()
                .baseUrl("http://dummy.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit.create(IdentityProviderBackend::class.java)
    }

    fun getIdentity(identityUrl: String): IdentityObject? {
        val backend = initializeBackend()
        val response = backend.getIdentity(identityUrl).execute()
        if (response.isSuccessful) {
            response.body()?.let {
                println(it.status)
                when (it?.status) {
                    IdentityResponse.Status.DONE -> return it.token!!.identityObject.value
                    IdentityResponse.Status.PENDING -> return null
                    else -> throw Exception(it.detail ?: "Unknown error")
                }
            }
        }
        throw Exception(response.message())
    }

    suspend fun fetch(identityUrl: String, retryDelay: Long = 10000): IdentityObject {
        while (true) {
            val response = getIdentity(identityUrl);
            if (response != null) {
                return response
            }
            // Try again after retryDelay
            delay(retryDelay)
        }
    }

    fun getFromRecovery(recoveryUrl: String): IdentityObject {
        val backend = initializeBackend()
        val response = backend.recoverIdentity(recoveryUrl).execute()
        if (response.isSuccessful) {
            response.body()?.let { return it.value }
        }
        throw Exception(response.message())
    }
}