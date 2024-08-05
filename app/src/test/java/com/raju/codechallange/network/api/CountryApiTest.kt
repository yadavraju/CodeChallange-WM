package com.raju.codechallange.network.api

import com.raju.codechallange.base.BaseServiceTest
import com.raju.codechallange.network.BASE_URL
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Assert
import org.junit.Test

const val STATUS_200 = "mock-responses/get-country-list.json"

class CountryApiTest : BaseServiceTest<CountryApi>(CountryApi::class) {

    override val baseUrl: String
        get() = BASE_URL

    @Test
    fun requestLiveGetCountries() = runTest {
        serviceLive.getCountries().also {
            Assert.assertNotNull(it)
            Assert.assertEquals(249, it.size)
            Assert.assertEquals("Afghanistan", it[0].name)
        }
    }

    @Test
    fun requestMockGetCountry() = runTest {
        enqueueResponse(STATUS_200)
        serviceMock.getCountries().also {
            val request: RecordedRequest = mockWebServer.takeRequest()
            Assert.assertEquals("GET", request.method)
            Assert.assertEquals(
                "/peymano-wmt/32dcb892b06648910ddd40406e37fdab/raw/db25946fd77c5873b0303b858e861ce724e0dcd0/countries.json",
                request.path
            )
            Assert.assertEquals(249, it.size)
        }
    }
}