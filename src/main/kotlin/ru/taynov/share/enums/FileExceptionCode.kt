package ru.taynov.share.enums

import org.springframework.http.HttpStatus
import ru.taynov.share.exeption.ApiException

enum class FileExceptionCode(
    private val errorCode: String,
    private val errorMessage: String,
    private val httpStatusCode: HttpStatus,
) {
    INVALID_FILE_SIZE("INVALID_FILE_SIZE", "Загружаемый файл слишком большой",
        HttpStatus.BAD_REQUEST,),
    INVALID_FILE_TYPE("INVALID_FILE_TYPE", "Файл данного формата не доступен для загрузки",
        HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    FILE_LIMIT_EXCEEDED("FILE_LIMIT_EXCEEDED", "Количество публикуемых файлов более 20",
        HttpStatus.BAD_REQUEST),
    ERROR_EMPTY_FILE("ERROR_EMPTY_FILE", "Публикуемый файл не выбран", HttpStatus.BAD_REQUEST);

    fun getException(): ApiException {
        return ApiException(errorCode, httpStatusCode, errorMessage)
    }
}