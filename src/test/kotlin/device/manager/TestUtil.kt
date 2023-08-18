package device.manager

import device.manager.device.Device
import device.manager.device.DeviceDto
import device.manager.user.User
import device.manager.user.UserDto
import java.time.LocalDate
import java.util.UUID

const val serialNumber = "SN123"
const val phoneNumber = "1234567890"
const val model = "Model X"
val deviceId: UUID = UUID.randomUUID()
val userId: UUID = UUID.randomUUID()

fun buildDevice() = Device(id = deviceId, serialNumber = serialNumber, model = model, phoneNumber = phoneNumber)
fun buildDeviceDto() = DeviceDto(serialNumber = serialNumber, phoneNumber = phoneNumber, model = model)
fun buildSavedDeviceDto() = DeviceDto(id = deviceId, serialNumber = serialNumber, phoneNumber = phoneNumber, model = model)

const val firstName = "John"
const val lastName = "Doe"
const val address = "123 Main St"
val birthday: LocalDate = LocalDate.parse("1990-01-01")

fun buildUser() = User(id = userId, firstName = firstName, lastName = lastName, address = address, birthday = birthday, device = null)
fun buildUserDto() = UserDto(firstName = firstName, lastName = lastName, address = address, birthday = birthday)
fun buildSavedUserDto() = UserDto(id = userId, firstName = firstName, lastName = lastName, address = address, birthday = birthday)

fun buildUserWithDevice() = User(id = userId, firstName = firstName, lastName = lastName, address = address, birthday = birthday, device = buildDevice())

fun buildSavedUserDtoWithDevice() = UserDto(id = userId, firstName = firstName, lastName = lastName, address = address, birthday = birthday, device = buildSavedDeviceDto())
