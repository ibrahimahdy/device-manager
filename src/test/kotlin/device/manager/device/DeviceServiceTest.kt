package device.manager.device

import device.manager.buildDevice
import device.manager.buildDeviceDto
import device.manager.buildSavedDeviceDto
import device.manager.serialNumber
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.dao.DataIntegrityViolationException

@ExtendWith(MockitoExtension::class)
class DeviceServiceTest {

    @Mock
    private lateinit var deviceRepository: DeviceRepository

    @InjectMocks
    private lateinit var deviceService: DeviceService

    @Test
    fun `should create device with valid input`() {
        `when`(deviceRepository.existsBySerialNumber(serialNumber)).thenReturn(false)
        `when`(deviceRepository.save(any())).thenReturn(buildDevice())

        val result = deviceService.createDevice(buildDeviceDto())

        assertThat(result).isEqualTo(buildSavedDeviceDto())
    }

    @Test
    fun `should throw exception when creating device with duplicate serial number`() {
        `when`(deviceRepository.existsBySerialNumber(serialNumber)).thenReturn(true)

        assertThatThrownBy {
            deviceService.createDevice(buildDeviceDto())
        }
            .isInstanceOf(DataIntegrityViolationException::class.java)
            .hasMessage("Device with the same serial number already exists.")
    }
}
