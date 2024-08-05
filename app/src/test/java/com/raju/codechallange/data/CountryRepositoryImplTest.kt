package com.raju.codechallange.data

import com.raju.codechallange.base.MockkUnitTest
import com.raju.codechallange.network.api.CountryApi
import com.raju.codechallange.network.model.Country
import com.raju.codechallange.network.model.Currency
import com.raju.codechallange.network.model.Language
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CountryRepositoryImplTest : MockkUnitTest() {

    private lateinit var countryRepository: CountryRepositoryImpl

    @RelaxedMockK
    private lateinit var countryApi: CountryApi

    @Before
    override fun onCreate() {
        countryRepository = CountryRepositoryImpl(countryApi)
    }

    @Test
    fun `getCountryList should return country list on success`() = runTest {
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
        coEvery { countryApi.getCountries() } returns countryList

        // When
        val result = countryRepository.getCountryList()

        // Then
        assertEquals(Result.success(countryList), result)
        coVerify { countryApi.getCountries() }
    }

    @Test
    fun `getCountryList should return error on failure`() = runTest {
        // Given
        val exception = Exception("Error")
        coEvery { countryApi.getCountries() } throws exception

        // When
        val result = countryRepository.getCountryList()

        // Then
        assertEquals(Result.failure<List<Country>>(exception), result)
        coVerify { countryApi.getCountries() }
    }
}