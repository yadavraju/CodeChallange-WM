package com.raju.codechallange.domain.exception

import com.raju.codechallange.domain.annotation.ExceptionType

sealed class BaseException(
    open val code: Int,
    @ExceptionType val type: Int,
    override val message: String?
) : Throwable(message) {

    data class AlertException(
        override val code: Int,
        override val message: String,
        val title: String? = null
    ) : BaseException(code, ExceptionType.ALERT, message)

    data class SnackBarException(
        override val code: Int = -1,
        override val message: String
    ) : BaseException(code, ExceptionType.SNACK, message)

    data class ToastException(
        override val code: Int,
        override val message: String
    ) : BaseException(code, ExceptionType.TOAST, message)

    data class DialogException(
        override val code: Int,
        val dialog: Dialog
    ) : BaseException(code, ExceptionType.DIALOG, null)

    data class OnPageException(
        override val code: Int,
        override val message: String
    ) : BaseException(code, ExceptionType.ON_PAGE, message)
}
