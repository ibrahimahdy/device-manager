package device.manager.exception

/**
 * Data class representing an error response.
 *
 * @property errorCode The HTTP status code of the error.
 * @property message A map of error messages. The keys are the names of the fields that caused
 * the validation error, and the values are the corresponding error messages.
 */
data class ErrorResponse(val errorCode: Int, val message: Map<String, String>)
