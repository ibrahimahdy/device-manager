package device.manager.user

import com.fasterxml.jackson.annotation.JsonInclude
import device.manager.device.DeviceDto
import java.time.LocalDate
import java.util.UUID

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserDto(
    val id: UUID?,
    val firstName: String,
    val lastName: String,
    val address: String,
    val birthday: LocalDate,
    val device: DeviceDto?
) {
    fun toEntity() = User(firstName = firstName, lastName = lastName, address = address, birthday = birthday)
}
