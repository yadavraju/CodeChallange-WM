package com.raju.codechallange.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

abstract class UseCase<in P, R> {
    operator fun invoke(parameters: P? = null): Flow<R> = execute(parameters)
        .flowOn(Dispatchers.IO)

    protected abstract fun execute(params: P? = null): Flow<R>

    // Clear anything when call different viewModelScope
    fun onCleared() {}
}
