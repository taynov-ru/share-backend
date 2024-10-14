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
    ERROR_EMPTY_FILE("ERROR_EMPTY_FILE", "Публикуемые файлы не выбраны", HttpStatus.BAD_REQUEST),
    FILE_DOWNLOAD_LIMIT_EXCEEDED("FILE_DOWNLOAD_LIMIT_EXCEEDED", "Превышен лимит скачиваний файла",
        HttpStatus.BAD_REQUEST),
    FILE_NOT_FOUND("FILE_NOT_FOUND", "Файл не найден", HttpStatus.NOT_FOUND),
    PUBLICATION_NOT_FOUND("PUBLICATION_NOT_FOUND", "Публикация не найдена", HttpStatus.NOT_FOUND),
    STORAGE_ERROR("STORAGE_ERROR", "Ошибка сохранения/извлечения/удаления файла",
        HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_CANNOT_BE_DELETED("FILE_CANNOT_BE_DELETED", "Файл не может быть удален",
        HttpStatus.BAD_REQUEST),
    PASSWORD_DOES_NOT_MATCH("PASSWORD_DOES_NOT_MATCH", "Введите корректный пароль",
        HttpStatus.BAD_REQUEST),
    LINK_ALREADY_EXISTS("LINK_ALREADY_EXISTS",
        "Такое имя ссылки уже используется. Пожалуйста, выберите другое имя", HttpStatus.BAD_REQUEST);

    fun getException(): ApiException {
        return ApiException(errorCode, httpStatusCode, errorMessage)
    }
}