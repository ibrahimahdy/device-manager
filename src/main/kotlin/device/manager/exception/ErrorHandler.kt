package device.manager.exception

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@ControllerAdvice
class ErrorHandler {

    /**
     * Exception handler for validation exceptions. It collects all the field errors,
     * then creates and returns a response with the error details.
     *
     * @param ex The exception thrown due to invalid arguments.
     * @return ResponseEntity containing the error response and the HTTP status.
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException): ResponseEntity<Any> {
        val errors = mutableMapOf<String, String>()
        ex.bindingResult.allErrors.forEach { error ->
            val fieldName = if (error is FieldError) error.field else error.objectName
            val errorMessage = error.defaultMessage
            errors[fieldName] = errorMessage.toString()
        }
        val errorResponse = ErrorResponse(HttpStatus.BAD_REQUEST.value(), errors)
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    /**
     * Exception handler for malformed or invalid request body. It creates and returns a response
     * with a predefined error message and HTTP status code.
     *
     * @param ex The exception thrown due to invalid or unreadable message content.
     * @return ResponseEntity containing the error response and the HTTP status.
     */
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException): ResponseEntity<Any> {
        val errorResponse = ErrorResponse(HttpStatus.BAD_REQUEST.value(), mapOf("message" to "Invalid request body."))
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(ex: EntityNotFoundException): ResponseEntity<Any> {
        val errorMessage = ex.message ?: "Item not found"
        val errorResponse = ErrorResponse(HttpStatus.NOT_FOUND.value(), mapOf("message" to errorMessage))
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(DeviceAlreadyAssignedException::class, DataIntegrityViolationException::class)
    fun handleDeviceAlreadyAssignedException(ex: Throwable): ResponseEntity<Any> {
        val errormessage = ex.message ?: "Conflict violation"
        val errorResponse = ErrorResponse(HttpStatus.CONFLICT.value(), mapOf("message" to errormessage))
        return ResponseEntity(errorResponse, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(ex: MethodArgumentTypeMismatchException): ResponseEntity<Any> {
        val errorMessage = "Invalid argument type for parameter: ${ex.name}"
        val errorResponse = ErrorResponse(HttpStatus.BAD_REQUEST.value(), mapOf("message" to errorMessage))
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }
}
