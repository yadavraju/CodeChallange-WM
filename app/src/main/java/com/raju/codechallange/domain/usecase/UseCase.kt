package com.raju.codechallange.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class UseCase<in P, R> {
    operator fun invoke(parameters: P? = null): Flow<R> = execute(parameters)
        .flowOn(Dispatchers.IO)

    protected abstract fun execute(params: P? = null): Flow<R>

    // Clear anything when call different viewModelScope
    fun onCleared() {}
}


abstract class BaseUseCase<in Params, out Result>(
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    /**
     * Executes the use case with the given parameters.
     * This function handles threading, ensuring that the execution
     * occurs on the specified [coroutineDispatcher].
     */
    suspend operator fun invoke(params: Params? = null): Result {
        return withContext(coroutineDispatcher) {
            execute(params)
        }
    }

    /**
     * This function should be overridden by subclasses to implement the actual use case logic.
     * The [execute] function will be called on the specified [coroutineDispatcher].
     */
    protected abstract suspend fun execute(params: Params?): Result
}
