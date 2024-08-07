package com.raju.codechallange.ui.viewmodel

import com.raju.codechallange.domain.exception.BaseException
import com.raju.codechallange.domain.usecase.country.GetCountryUseCase
import com.raju.codechallange.network.model.Country
import com.raju.codechallange.ui.base.BaseViewModel
import com.raju.codechallange.ui.base.ViewState
import com.raju.codechallange.ui.base.toBaseException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class CountryViewState(
    override val isLoading: Boolean = false,
    override val exception: BaseException? = null,
    val countryList: List<Country> = emptyList(),
) : ViewState(isLoading, exception)

@HiltViewModel
class CountryViewModel @Inject constructor(
    private val getCountryUseCase: GetCountryUseCase,
) : BaseViewModel() {

    private val _state = MutableStateFlow(CountryViewState(isLoading = true))
    override val state: StateFlow<CountryViewState>
        get() = _state

    init {
        fetchCountries()
    }

    private fun fetchCountries() {
        showLoading()
        safeLunch {
            getCountryUseCase.invoke()
                .onSuccess { countryList ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            countryList = countryList
                        )
                    }
                }.onFailure { throwable ->
                    println("Raju Error: $throwable")
                    showError(throwable.toBaseException())
                }
        }
    }

    private fun showLoading() {
        _state.update { it.copy(isLoading = true) }
    }

    override fun hideLoading() {
        _state.update { it.copy(isLoading = false) }
    }

    override fun showError(error: BaseException) {
        if (_state.value.exception == null) {
            _state.update {
                it.copy(isLoading = false, exception = error)
            }
        }
    }
}
