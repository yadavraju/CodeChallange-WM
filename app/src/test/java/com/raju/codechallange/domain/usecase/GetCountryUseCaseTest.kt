package com.raju.codechallange.domain.usecase

import com.raju.codechallange.base.MockkUnitTest

import com.raju.codechallange.domain.repository.CountryRepository
import com.raju.codechallange.domain.usecase.country.GetCountryUseCase
import com.raju.codechallange.network.model.Country
import com.raju.codechallange.network.model.Currency
import com.raju.codechallange.network.model.Language
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetCountryUseCaseTest : MockkUnitTest() {

    private lateinit var getCountryUseCase: GetCountryUseCase
    private val countryRepository: CountryRepository = mockk()

    @Before
    override fun onCreate() {
        getCountryUseCase = GetCountryUseCase(countryRepository)
    }

    @Test
    fun `invoke should return country list on success`() = runTest {
        // Given
        val countryList: List<Country> = listOf(
            Country(
                capital = "Kabul",
                code = "AF",
                currency = Currency(
                    code = "AFN",
                    name = "Afghan afghani",
                    symbol = "؋"
                ),
                flag = "https://restcountries.eu/data/afg.svg",
                language = Language(
                    code = "ps",
                    name = "Pashto"
                ),
                name = "Afghanistan",
                region = "AS"
            )
        )
        coEvery { countryRepository.getCountryList() } returns Result.success(countryList)

        // When
        val result = getCountryUseCase.invoke()

        // Then
        assertEquals(Result.success(countryList), result)
        coVerify { countryRepository.getCountryList() }
    }

    @Test
    fun `invoke should return error on failure`() = runTest {
        // Given
        val exception = Exception("Error")
        coEvery { countryRepository.getCountryList() } returns Result.failure(exception)

        // When
        val result = getCountryUseCase.invoke()

        // Then
        assertEquals(Result.failure<List<Country>>(exception), result)
        coVerify { countryRepository.getCountryList() }
    }
}