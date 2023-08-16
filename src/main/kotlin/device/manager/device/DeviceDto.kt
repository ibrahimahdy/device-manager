package device.manager.device

import java.util.UUID

data class DeviceDto(
    val id: UUID? = null,
    val serialNumber: String,
    val model: String,
    val phoneNumber: String,
) {
    fun toEntity() = Device(serialNumber = serialNumber, model = model, phoneNumber = phoneNumber)
}
