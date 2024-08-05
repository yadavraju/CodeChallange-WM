package com.raju.codechallange.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

// Builder pattern for Retrofit
// This class is used to build a Retrofit instance with custom configurations
@Singleton
class RetrofitBuilder @Inject constructor() {

    private var connectionTimeout = CONNECT_TIMEOUT
    private var writeTimeout = WRITE_TIMEOUT
    private var readTimeout = READ_TIMEOUT
    private var okHttpClientBuilder: OkHttpClient.Builder? = null
    private var moshi: Moshi? = null
    private var interceptors = mutableListOf<Interceptor>()
    private var logEnable: Boolean = true // BuildConfig.DEBUG
    private var isSupportAuthorization = false
    private var authenticator: Authenticator? = null
    private var baseUrl: String = BASE_URL

    /**
     * Customize time out
     * @param connectionTimeout timeout for connection OK Http client
     * @param writeTimeout timeout for write data
     * @param readTimeout timeout for read data
     */
    fun setTimeout(
        connectionTimeout: Long = CONNECT_TIMEOUT,
        writeTimeout: Long = WRITE_TIMEOUT,
        readTimeout: Long = READ_TIMEOUT
    ): RetrofitBuilder {
        this.connectionTimeout = connectionTimeout
        this.writeTimeout = writeTimeout
        this.readTimeout = readTimeout
        return this
    }

    /**
     * User customize ok http client
     * @param okHttpClientBuilder
     */
    fun setOkHttpClientBuilder(okHttpClientBuilder: OkHttpClient.Builder): RetrofitBuilder {
        this.okHttpClientBuilder = okHttpClientBuilder
        return this
    }

    /**
     * User custom moshi builder
     * @param Moshi
     */
    fun setMoshiBuilder(moshi: Moshi): RetrofitBuilder {
        this.moshi = moshi
        return this
    }

    /**
     * add custom interceptor for ok http client
     * @param interceptor is a interceptor for ok http client
     */
    fun addInterceptors(vararg interceptor: Interceptor): RetrofitBuilder {
        interceptors.addAll(interceptor)
        return this
    }

    /**
     * Customize show or hide logging
     * @param enable is status for logs
     */
    fun loggingEnable(enable: Boolean): RetrofitBuilder {
        this.logEnable = enable
        return this
    }

    /**
     * Support default Authorization
     * @param enable is status support
     */
    fun supportAuthorization(enable: Boolean): RetrofitBuilder {
        this.isSupportAuthorization = enable
        return this
    }

    /**
     * Customize authorization
     * @param authenticator
     */
    fun setCustomAuthorization(authenticator: Authenticator): RetrofitBuilder {
        this.authenticator = authenticator
        return this
    }

    /**
     * Customize base url
     * @param baseUrl is base url for ok http client
     */
    fun setBaseURL(baseUrl: String): RetrofitBuilder {
        this.baseUrl = baseUrl
        return this
    }

    /**
     * Make a Retrofit
     */
    fun build(): Retrofit {
        val clientBuilder = okHttpClientBuilder ?: OkHttpClient.Builder().apply {
            connectTimeout(connectionTimeout, TimeUnit.SECONDS)
            writeTimeout(writeTimeout, TimeUnit.SECONDS)
            readTimeout(readTimeout, TimeUnit.SECONDS)

            if (logEnable) {
                addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                )
            }

            interceptors.forEach { addInterceptor(it) }
        }

        val moshi = moshi ?: Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(clientBuilder.build())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}
