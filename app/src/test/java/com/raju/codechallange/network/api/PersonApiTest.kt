package com.raju.codechallange.network.api

import com.raju.codechallange.base.BaseServiceTest
import com.raju.codechallange.network.BASE_URL
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Assert
import org.junit.Test

const val STATUS_200 = "mock-responses/get-person-list.json"

class PersonApiTest : BaseServiceTest<PersonApi>(PersonApi::class) {

    override val baseUrl: String
        get() = BASE_URL

    @Test
    fun requestLiveGetPersonList() = runTest {
        serviceLive.getPersonList().also {
            Assert.assertNotNull(it)
            Assert.assertEquals(9, it.people.size)
            Assert.assertEquals("Russ", it.people[0].name)
        }
    }

    @Test
    fun requestMockGetPersonList() = runTest {
        enqueueResponse(STATUS_200)
        serviceMock.getPersonList().also {
            val request: RecordedRequest = mockWebServer.takeRequest()
            Assert.assertEquals("GET", request.method)
            Assert.assertEquals(
                "/russellbstephens/9e528b12fd1a45a7ff4e4691adcddf10/raw/5ec8ce76460e8f29c9b0f99f3bf3450b06696482/people.json",
                request.path
            )
            Assert.assertEquals(9, it.people.size)
        }
    }
}