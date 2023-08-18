package device.manager.user

import device.manager.buildDevice
import device.manager.buildSavedUserDto
import device.manager.buildSavedUserDtoWithDevice
import device.manager.buildUser
import device.manager.buildUserDto
import device.manager.buildUserWithDevice
import device.manager.device.DeviceRepository
import device.manager.deviceId
import device.manager.exception.DeviceAlreadyAssignedException
import device.manager.exception.EntityNotFoundException
import device.manager.userId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var deviceRepository: DeviceRepository

    @InjectMocks
    private lateinit var userService: UserService

    @Test
    fun `should create user`() {
        `when`(userRepository.save(any())).thenReturn(buildUser())

        val result = userService.createUser(buildUserDto())

        assertThat(buildSavedUserDto()).isEqualTo(result)
    }

    @Test
    fun `should assign device to user with valid input`() {
        `when`(userRepository.findById(userId)).thenReturn(Optional.of(buildUser()))
        `when`(deviceRepository.findById(deviceId)).thenReturn(Optional.of(buildDevice()))
        `when`(userRepository.findFirstByDevice(buildDevice())).thenReturn(null)

        val result = userService.assignDeviceToUser(userId, deviceId)

        assertThat(buildSavedUserDtoWithDevice()).isEqualTo(result)
    }

    @Test
    fun `should throw exception when assigning user to device that is assigned`() {
        `when`(userRepository.findById(userId)).thenReturn(Optional.of(buildUser()))
        `when`(deviceRepository.findById(deviceId)).thenReturn(Optional.of(buildDevice()))
        `when`(userRepository.findFirstByDevice(buildDevice())).thenReturn(buildUserWithDevice())

        try {
            userService.assignDeviceToUser(userId, deviceId)
        } catch (ex: DeviceAlreadyAssignedException) {
            assertThat("Device is already assigned to another user.").isEqualTo(ex.message)
        }
    }

    @Test
    fun `should throw exception when assigning user that doesn't exist`() {
        `when`(userRepository.findById(userId)).thenReturn(Optional.empty())

        try {
            userService.assignDeviceToUser(userId, deviceId)
        } catch (ex: EntityNotFoundException) {
            assertThat("User not found").isEqualTo(ex.message)
        }
    }

    @Test
    fun `should list users with devices`() {
        val usersWithDevices = listOf(buildUser())

        `when`(userRepository.findUsersWithDevices()).thenReturn(usersWithDevices)

        val result = userService.getUsersWithDevices()

        assertThat(buildSavedUserDto()).isEqualTo(result[0])
    }
}
