package device.manager.device

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException

@SpringBootTest
class DeviceServiceTest {

    @Mock
    private lateinit var deviceRepository: DeviceRepository

    @InjectMocks
    private lateinit var deviceService: DeviceService

    @Test
    fun `Test createDevice with valid input`() {
        val deviceDto = DeviceDto(serialNumber = "SN123", phoneNumber = "1234567890", model = "Model X")
        val deviceEntity = deviceDto.toEntity()
        val savedDeviceEntity = deviceEntity.copy(id = java.util.UUID.randomUUID())

        `when`(deviceRepository.existsBySerialNumber(deviceDto.serialNumber)).thenReturn(false)
        `when`(deviceRepository.save(deviceEntity)).thenReturn(savedDeviceEntity)

        val result = deviceService.createDevice(deviceDto)

        assertThat(result).isEqualTo(savedDeviceEntity.toDto())
    }

    @Test
    fun `Test createDevice with duplicate serial number`() {
        val deviceDto = DeviceDto(serialNumber = "SN123", phoneNumber = "1234567890", model = "Model X")

        `when`(deviceRepository.existsBySerialNumber(deviceDto.serialNumber)).thenReturn(true)

        assertThatThrownBy {
            deviceService.createDevice(deviceDto)
        }
            .isInstanceOf(DataIntegrityViolationException::class.java)
            .hasMessage("Device with the same serial number already exists.")

        verify(deviceRepository, times(1)).existsBySerialNumber(deviceDto.serialNumber)
        verify(deviceRepository, never()).save(any())
    }
}
