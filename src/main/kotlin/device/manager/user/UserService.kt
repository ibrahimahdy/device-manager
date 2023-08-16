package device.manager.user

import device.manager.device.DeviceRepository
import device.manager.exception.DeviceAlreadyAssignedException
import device.manager.exception.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(private val userRepository: UserRepository, private val deviceRepository: DeviceRepository) {

    fun createUser(userDto: UserDto): UserDto {
        val user = userDto.toEntity()
        val savedUser = userRepository.save(user)
        return savedUser.toDto()
    }

    @Transactional
    fun assignDeviceToUser(userId: UUID, deviceId: UUID): UserDto {
        val user = userRepository.findById(userId).orElseThrow { EntityNotFoundException("User not found") }
        val device = deviceRepository.findById(deviceId).orElseThrow { EntityNotFoundException("Device not found") }

        // Check if the device is already assigned to another user
        val existingUserWithDevice = userRepository.findFirstByDevice(device)
        if ((existingUserWithDevice != null) && (existingUserWithDevice != user)) {
            throw DeviceAlreadyAssignedException("Device is already assigned to another user.")
        }

        user.device = device
        userRepository.save(user)

        return user.toDto()
    }

    fun getUsersWithDevices(): List<UserDto> {
        val usersWithDevices = userRepository.findUsersWithDevices()
        return usersWithDevices.map { user -> user.toDto() }
    }
}
