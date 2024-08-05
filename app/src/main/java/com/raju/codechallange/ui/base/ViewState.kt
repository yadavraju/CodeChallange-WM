package com.raju.codechallange.ui.base

import com.raju.codechallange.domain.exception.BaseException

open class ViewState(
    open val isLoading: Boolean = false,
    open val exception: BaseException? = null
)

fun Throwable.toBaseException(): BaseException {
    return when (this) {
        is BaseException -> this
        else -> BaseException.ToastException(code = -1, message = this.message ?: "")
    }
}
