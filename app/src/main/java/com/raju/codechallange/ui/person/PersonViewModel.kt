package com.raju.codechallange.ui.person

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raju.codechallange.domain.repository.PersonRepository
import com.raju.codechallange.network.model.PersonListResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class PersonState {
    data object Loading : PersonState()
    data class SuccessResponse(val personListResponse: PersonListResponse) : PersonState()
    data class Error(val message: String) : PersonState()
}

@HiltViewModel
class PersonViewModel @Inject constructor(private val personRepository: PersonRepository) :
    ViewModel() {

    private val _state = MutableStateFlow<PersonState>(PersonState.Loading)
    val state: StateFlow<PersonState>
        get() = _state

    init {
        getPersonList()
    }

    private fun getPersonList() {
        viewModelScope.launch {
            personRepository.getPersonList().onSuccess { data ->
                _state.update { PersonState.SuccessResponse(personListResponse = data) }
            }.onFailure { error ->
                _state.update { PersonState.Error(message = error.toString()) }
            }
        }
    }
}