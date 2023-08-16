package device.manager.device

import jakarta.transaction.Transactional
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service

@Service
class DeviceService(private val deviceRepository: DeviceRepository) {

    @Transactional
    fun createDevice(deviceDto: DeviceDto): DeviceDto {
        if (deviceRepository.existsBySerialNumber(deviceDto.serialNumber)) {
            throw DataIntegrityViolationException("Device with the same serial number already exists.")
        }

        val device = deviceDto.toEntity()
        val savedDevice = deviceRepository.save(device)
        return savedDevice.toDto()
    }
}
