package device.manager.device

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

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
    fun `Test creating a device`() {
        val deviceDto = DeviceDto(serialNumber = "SN123", phoneNumber = "1234567890", model = "Model X")
        val createdDevice = DeviceDto(id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"), serialNumber = "SN123", phoneNumber = "1234567890", model = "Model X")

        `when`(deviceService.createDevice(deviceDto)).thenReturn(createdDevice)
        val expected = """{"id":"550e8400-e29b-41d4-a716-446655440000","serialNumber":"SN123","model":"Model X","phoneNumber":"1234567890"}"""

        val response = mockMvc.perform(
            post("/devices")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(deviceDto)),
        )
            .andExpect(status().isCreated)
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
            .response
            .contentAsString

        assertThat(response).isEqualTo(expected)
    }
}
