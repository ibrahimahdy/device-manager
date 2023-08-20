package device.manager.device

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import device.manager.buildDeviceDto
import device.manager.buildSavedDeviceDto
import device.manager.model
import device.manager.phoneNumber
import device.manager.serialNumber
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
class DeviceControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var deviceService: DeviceService

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `should create a device`() {
        `when`(deviceService.createDevice(buildDeviceDto())).thenReturn(buildSavedDeviceDto())

        mockMvc.perform(
            post("/devices")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(buildDeviceDto())),
        )
            .andExpect(status().isCreated)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(buildSavedDeviceDto())))
    }

    @Test
    fun `should return bad request for invalid user input`() {
        val invalidDeviceDto = DeviceDto(serialNumber = "", phoneNumber = "123", model = "x")

        mockMvc.perform(
            post("/devices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDeviceDto)),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should return conflict when trying to create device with duplicate serial number`() {
        `when`(deviceService.createDevice(buildDeviceDto())).thenThrow(DataIntegrityViolationException("Device with the same serial number already exists."))

        val errorMessage = """{"errorCode":409,"message":{"message":"Device with the same serial number already exists."}}"""

        mockMvc.perform(
            post("/devices")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(buildDeviceDto())),
        )
            .andExpect(status().isConflict)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.content().string(errorMessage))
    }
}
