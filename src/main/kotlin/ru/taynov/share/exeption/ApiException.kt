package ru.taynov.share.exeption

import java.util.function.Supplier
import org.springframework.http.HttpStatus

class ApiException(
    message: String,
    val httpStatus: HttpStatus,
    val code: String,
    cause: Throwable? = null
): RuntimeException(message, cause), Supplier<ApiException>  {

    override fun get(): ApiException {
        return this
    }

    override fun toString(): String {
        return "ApiException(code='$code')"
    }
}