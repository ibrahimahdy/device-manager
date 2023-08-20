package device.manager.util

import device.manager.device.Device
import device.manager.device.DeviceRepository
import device.manager.user.User
import device.manager.user.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class DataInitializer(
    private val deviceRepository: DeviceRepository,
    private val userRepository: UserRepository
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        val device1 = Device(serialNumber = "SN123", phoneNumber = "1234567890", model = "Model A")
        val device2 = Device(serialNumber = "SN456", phoneNumber = "9876543210", model = "Model B")
        deviceRepository.saveAll(listOf(device1, device2))

        val user1 = User(firstName = "John", lastName = "Doe", address = "123 Main St", birthday = LocalDate.of(1990, 1, 1))
        val user2 = User(firstName = "Jane", lastName = "Smith", address = "456 Elm St", birthday = LocalDate.of(1985, 5, 15))

        userRepository.saveAll(listOf(user1, user2))
    }
}
