package com.raju.codechallange.ui.viewmodel

import com.raju.codechallange.base.MockkUnitTest
import com.raju.codechallange.domain.repository.PersonRepository
import com.raju.codechallange.network.model.Person
import com.raju.codechallange.network.model.PersonListResponse
import com.raju.codechallange.ui.person.PersonState
import com.raju.codechallange.ui.person.PersonViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PersonViewModelTest : MockkUnitTest() {

    @RelaxedMockK
    private lateinit var repository: PersonRepository

    private lateinit var viewModel: PersonViewModel

    @Before
    override fun onCreate() {
        super.onCreate()
    }

    @Test
    fun `fetchPerson should update state success`() = runTest {
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
        coEvery { repository.getPersonList() } returns Result.success(PersonListResponse(people))

        // When
        viewModel = PersonViewModel(repository)

        // Then
        val state: PersonState = viewModel.state.first()
        assertTrue(state is PersonState.SuccessResponse)
        coVerify { repository.getPersonList() }
    }

    @Test
    fun `fetchCountries should update state with error on failure`() = runTest {
        // Given
        val exception = Exception("Error")
        coEvery { repository.getPersonList() } returns Result.failure(exception)

        // When
        viewModel = PersonViewModel(repository)

        // Then
        val state = viewModel.state.first()
        assertTrue(state is PersonState.Error)
        coVerify { repository.getPersonList() }
    }
}