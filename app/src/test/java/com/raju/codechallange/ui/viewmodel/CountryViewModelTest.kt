package com.raju.codechallange.ui.viewmodel

import com.raju.codechallange.base.MockkUnitTest
import com.raju.codechallange.domain.exception.BaseException
import com.raju.codechallange.domain.usecase.country.GetCountryUseCase
import com.raju.codechallange.network.model.Country
import com.raju.codechallange.network.model.Currency
import com.raju.codechallange.network.model.Language
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CountryViewModelTest : MockkUnitTest() {

    @RelaxedMockK
    private lateinit var getCountryUseCase: GetCountryUseCase

    private lateinit var viewModel: CountryViewModel

    @Before
    override fun onCreate() {
        super.onCreate()
        viewModel = CountryViewModel(getCountryUseCase)
    }

    @Test
    fun `fetchCountries should update state with country list on success`() = runTest {
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
        coEvery { getCountryUseCase.invoke() } returns Result.success(countryList)

        // When
        //viewModel = CountryViewModel(getCountryUseCase)

        // Then
        val state = viewModel.state.first()
        assertEquals(false, state.isLoading)
        assertEquals(countryList, state.countryList)
        assertEquals(null, state.exception)
        coVerify { getCountryUseCase.invoke() }
    }

    @Test
    fun `fetchCountries should update state with error on failure`() = runTest {
        // Given
        val exception = BaseException.ToastException(-1, "Error")
        coEvery { getCountryUseCase.invoke() } returns Result.failure(exception)

        // When
        //viewModel = CountryViewModel(getCountryUseCase)

        // Then
        val state = viewModel.state.first()
        assertEquals(false, state.isLoading)
        assertEquals(emptyList<Country>(), state.countryList)
        assertEquals(exception, state.exception)
        coVerify { getCountryUseCase.invoke() }
    }

    @Test
    fun `hideLoading should update state to not loading`() = runTest {
        // Given
        //viewModel = CountryViewModel(getCountryUseCase)

        // When
        viewModel.hideLoading()

        // Then
        val state = viewModel.state.first()
        assertEquals(false, state.isLoading)
    }

}