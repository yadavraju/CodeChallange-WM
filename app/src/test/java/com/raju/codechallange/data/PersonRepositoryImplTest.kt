package com.raju.codechallange.data

import com.raju.codechallange.base.MockkUnitTest
import com.raju.codechallange.domain.repository.PersonRepository
import com.raju.codechallange.network.api.PersonApi
import com.raju.codechallange.network.model.Person
import com.raju.codechallange.network.model.PersonListResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PersonRepositoryImplTest : MockkUnitTest() {

    private lateinit var personRepository: PersonRepository

    @RelaxedMockK
    private lateinit var personApi: PersonApi

    @Before
    override fun onCreate() {
        personRepository = PersonRepositoryImpl(personApi)
    }

    @Test
    fun `getPersonList should return person list on success`() = runTest {
        // Given

        val people = listOf(
            Person(name = "Russ", language = "swift"),
            Person(name = "Wes", language = "dart"),
            Person(name = "Tania", language = "kotlin"),
            Person(name = "Robert", language = "kotlin"),
            Person(name = "Leilah", language = "swift"),
            Person(name = "Bastien", language = "swift"),
            Person(name = "Alan", language = "swift"),
            Person(name = "Lee", language = "swift"),
            Person(name = "Margie", language = null)
        )
        val peopleRes = PersonListResponse(people)
        coEvery { personApi.getPersonList() } returns PersonListResponse(people)

        // When
        val result = personRepository.getPersonList()

        // Then
        assertEquals(Result.success(peopleRes), result)
        coVerify { personApi.getPersonList() }
    }

    @Test
    fun `getPersonList should return error on failure`() = runTest {
        // Given
        val exception = Exception("Error")
        coEvery { personApi.getPersonList() } throws exception

        // When
        val result = personRepository.getPersonList()

        // Then
        assertEquals(Result.failure<PersonListResponse>(exception), result)
        coVerify { personApi.getPersonList() }
    }
}