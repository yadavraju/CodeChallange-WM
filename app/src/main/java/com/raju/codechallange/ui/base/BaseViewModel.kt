package com.raju.codechallange.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raju.codechallange.domain.usecase.UseCase
import com.raju.codechallange.domain.exception.BaseException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseViewModel(
    private vararg val useCases: UseCase<*, *>?
) : ViewModel() {

    private var job: Job? = null
    private var callApi: suspend CoroutineScope.() -> Unit = {}

    abstract val state: StateFlow<ViewState>

    open fun showError(error: BaseException) {}

    open fun hideLoading() {}

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        hideLoading()
        val errorResponse = if (exception is BaseException) {
            exception
        } else {
            BaseException.ToastException(
                code = -1,
                message = exception.toString(),
            )
        }
        showError(errorResponse)
    }

    fun safeLunch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        api: suspend CoroutineScope.() -> Unit,
    ) {
        viewModelScope.launch(context + coroutineExceptionHandler, start) {
            callApi = api

            job = launch {
                callApi.invoke(this)
            }

            job?.join()
        }
    }

    override fun onCleared() {
        useCases.forEach { it?.onCleared() }
        job?.cancel()
        job = null
        super.onCleared()
    }
}
