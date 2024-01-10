package com.example.android_sdk_example.identity_object

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
        val detail: String,
    ) {
        enum class Status(value: String) {
            DONE("done"),
            PENDING("pending"),
            ERROR("token"),
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
    }

    fun getIdentity(identityUrl: String): IdentityObject? {
        val retrofit =
            Retrofit.Builder()
                .baseUrl("http://dummy.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val backend = retrofit.create(IdentityProviderBackend::class.java)

        val response = backend.getIdentity(identityUrl).execute()
        if (response.isSuccessful) {
            response.body()?.let{
                when (it.status) {
                    IdentityResponse.Status.DONE -> return it.token!!.identityObject.value
                    IdentityResponse.Status.PENDING -> return null
                    IdentityResponse.Status.ERROR -> throw Exception(it.detail)
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
}