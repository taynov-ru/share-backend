package ru.taynov.share.exeption

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import ru.taynov.openapi.model.ErrorGen
import ru.taynov.share.utils.INTERNAL_SERVER_ERROR
import ru.taynov.share.utils.INVALID_PARAMETERS
import ru.taynov.share.utils.METHOD_ARGUMENT_NOT_VALID_ERROR
import ru.taynov.share.utils.VALIDATION_ERROR
import javax.validation.ConstraintViolationException
import javax.validation.ValidationException

@ControllerAdvice
class CommonExceptionHandler {

    @ExceptionHandler(ApiException::class)
    fun handleApiException(ex: ApiException): ResponseEntity<ErrorGen> {
        log.warn("Exception was thrown: $ex", ex)
        val error = ErrorGen(ex.code, ex.message)
        return ResponseEntity(error, ex.httpStatus)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ErrorGen> {
        log.error("Exception was thrown: $ex", ex)
        val error = ErrorGen(INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера")
        return ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException): ResponseEntity<ErrorGen> {
        log.warn("MethodArgumentNotValidException was thrown: $ex", ex)
        val error = ErrorGen(METHOD_ARGUMENT_NOT_VALID_ERROR, "Ошибка валидации: ${ex.message}")
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(ex: ValidationException): ResponseEntity<ErrorGen> {
        log.error("Exception was thrown: $ex", ex)
        val error = ErrorGen(VALIDATION_ERROR, "Ошибка валидации: ${ex.message}")
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(ex: ConstraintViolationException): ResponseEntity<ErrorGen> {
        log.error("ConstraintViolationException was thrown: $ex", ex)
        val error = ErrorGen(INVALID_PARAMETERS, "Отсутствуют обязательные параметры")
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(CommonExceptionHandler::class.java)
    }
}