package device.manager.user

import com.fasterxml.jackson.annotation.JsonInclude
import device.manager.device.DeviceDto
import jakarta.validation.constraints.NotBlank
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.util.UUID

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserDto(
    val id: UUID? = null,

    @field:NotBlank
    val firstName: String,

    @field:NotBlank
    val lastName: String,

    val address: String,

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val birthday: LocalDate,

    val device: DeviceDto? = null,
) {
    fun toEntity() = User(firstName = firstName, lastName = lastName, address = address, birthday = birthday)
}
