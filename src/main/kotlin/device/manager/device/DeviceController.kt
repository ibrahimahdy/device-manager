package device.manager.device

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/devices")
class DeviceController(private val deviceService: DeviceService) {

    @PostMapping
    fun createDevice(
        @Valid @RequestBody
        deviceDto: DeviceDto,
    ): ResponseEntity<DeviceDto> {
        val createdDevice = deviceService.createDevice(deviceDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDevice)
    }
}
