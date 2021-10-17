package com.josancamon19.producthunttest.network

import com.apollographql.apollo.ApolloClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

private var instance: ApolloClient? = null

fun apolloClient(): ApolloClient {
    if (instance != null) {
        return instance!!
    }
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthorizationInterceptor())
        .build()
    instance = ApolloClient.builder()
        .serverUrl("https://ph-graph-api-explorer.herokuapp.com/graphql")
        .okHttpClient(okHttpClient)
        .build()
    return instance!!
}

private class AuthorizationInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer Pmi_WfEQ_Ag4tNWbQtcF_qMl3mOlB3YKXuMp545JmUg")
            .build()

        return chain.proceed(request)
    }
}
