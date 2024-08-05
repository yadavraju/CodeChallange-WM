package com.example.weather.base

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weather.data.model.CurrentWeather
import com.example.weather.data.remote.factory.FlowCallAdapterFactory
import com.example.weather.data.remote.interceptor.HeaderInterceptor
import com.example.weather.data.remote.mapper.ExceptionMapper
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Rule
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.stream.Collectors
import kotlin.reflect.KClass

abstract class BaseServiceTest<S : Any>(service: KClass<S>) {

    lateinit var mockWebServer: MockWebServer
    private lateinit var okhttp: OkHttpClient
    private lateinit var flowCallAdapterFactory: FlowCallAdapterFactory

    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    abstract val baseUrl: String

    val serviceLive: S by lazy {
        require(baseUrl != "") { "baseUrl must be not empty" }

        Retrofit.Builder()
            .client(okhttp)
            .baseUrl(baseUrl)
            .addCallAdapterFactory(flowCallAdapterFactory)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(service)
    }

    val serviceMock: S by lazy {
        Retrofit.Builder()
            .client(okhttp)
            .baseUrl(mockWebServer.url(""))
            .addCallAdapterFactory(flowCallAdapterFactory)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(service)
    }

    @Before
    fun setUp() {
        mockWebServer = MockWebServer().apply {
            start()
        }
        //val headerInterceptor = mockk<HeaderInterceptor>()
        okhttp = OkHttpClient.Builder()
            .followSslRedirects(true)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
        val exceptionMapper = mockk<ExceptionMapper>(relaxed = true)
        flowCallAdapterFactory = FlowCallAdapterFactory.create(exceptionMapper)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    fun enqueueResponse(filePath: String) {
        val inputStream = javaClass.classLoader?.getResourceAsStream(filePath)
        val bufferSource = inputStream?.source()?.buffer() ?: return
        val mockResponse = MockResponse()

        mockWebServer.enqueue(
            mockResponse.setBody(
                bufferSource.readString(Charsets.UTF_8)
            )
        )
        println(
            "🍏 enqueueResponse() ${Thread.currentThread().name}," +
                    " ${bufferSource.readString(Charsets.UTF_8).length} $mockResponse"
        )
    }

    private fun <T : Any> Retrofit.create(service: KClass<T>): T = create(service.javaObjectType)

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getJson(fileName: String): String {
        val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
        return BufferedReader(InputStreamReader(inputStream)).lines()
            .collect(Collectors.joining("\n"))
    }
}
