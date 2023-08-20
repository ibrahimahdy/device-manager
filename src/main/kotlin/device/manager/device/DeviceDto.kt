package device.manager.device

import jakarta.validation.constraints.NotBlank
import java.util.UUID

data class DeviceDto(
    val id: UUID? = null,

    @field:NotBlank
    val serialNumber: String,

    val model: String,

    val phoneNumber: String,
) {
    fun toEntity() = Device(serialNumber = serialNumber, model = model, phoneNumber = phoneNumber)
}
