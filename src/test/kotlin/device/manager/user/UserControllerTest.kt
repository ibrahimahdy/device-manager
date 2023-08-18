package device.manager.user

import device.manager.buildSavedUserDto
import device.manager.buildSavedUserDtoWithDevice
import device.manager.buildUserDto
import device.manager.device.DeviceDto
import device.manager.deviceId
import device.manager.exception.DeviceAlreadyAssignedException
import device.manager.userId
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate
import java.util.UUID

@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userDtoJsonTester: JacksonTester<UserDto>

    @Autowired
    private lateinit var userDtoJsonTesterList: JacksonTester<List<UserDto>>

    @MockBean
    private lateinit var userService: UserService

    @Test
    fun `should create user with valid input`() {
        `when`(userService.createUser(buildUserDto())).thenReturn(buildSavedUserDto())

        mockMvc.perform(
            post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userDtoJsonTester.write(buildUserDto()).json),
        )
            .andExpect(status().isCreated)
            .andExpect(content().json(userDtoJsonTester.write(buildSavedUserDto()).json))
    }

    @Test
    fun `should return bad request for invalid user input`() {
        val invalidUserDto = UserDto(firstName = "", lastName = "", address = "", birthday = LocalDate.parse("1990-01-01"))

        mockMvc.perform(
            post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userDtoJsonTester.write(invalidUserDto).json),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should return bad request for invalid device id input`() {
        val invalidDeviceId = "invalid-device-id"
        val errorMessage = """{"errorCode":400,"message":{"message":"Invalid argument type for parameter: deviceId"}}"""

        mockMvc.perform(
            post("/users/$userId/assign-device/$invalidDeviceId"),
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().string(containsString(errorMessage)))
    }

    @Test
    fun `should assign device to user with valid input`() {
        `when`(userService.assignDeviceToUser(userId, deviceId)).thenReturn(buildSavedUserDtoWithDevice())

        mockMvc.perform(
            post("/users/$userId/assign-device/$deviceId"),
        )
            .andExpect(status().isOk)
            .andExpect(content().json(userDtoJsonTester.write(buildSavedUserDtoWithDevice()).json))
    }

    @Test
    fun `should return conflict when assigning user to device that is assigned`() {
        val errorMessage = """{"message":"Device is already assigned to another user."}"""

        `when`(userService.assignDeviceToUser(userId, deviceId)).thenThrow(DeviceAlreadyAssignedException("Device is already assigned to another user."))

        mockMvc.perform(
            post("/users/$userId/assign-device/$deviceId"),
        )
            .andExpect(status().isConflict)
            .andExpect(content().string(containsString(errorMessage)))
    }

    @Test
    fun `should list users with devices`() {
        val deviceId2 = UUID.randomUUID()

        val serialNumber = "SN123"
        val phoneNumber = "1234567890"
        val model = "Model X"

        val deviceDto = DeviceDto(id = deviceId, serialNumber = serialNumber, phoneNumber = phoneNumber, model = model)
        val deviceDto2 = DeviceDto(id = deviceId2, serialNumber = serialNumber, phoneNumber = phoneNumber, model = model)

        val usersWithDevices = listOf(
            UserDto(id = UUID.randomUUID(), firstName = "John", lastName = "Doe", address = "123 Main St", birthday = LocalDate.parse("1990-01-01"), device = deviceDto),
            UserDto(id = UUID.randomUUID(), firstName = "Jane", lastName = "Smith", address = "456 Elm St", birthday = LocalDate.parse("1985-05-15"), device = deviceDto2),
        )

        `when`(userService.getUsersWithDevices()).thenReturn(usersWithDevices)

        mockMvc.perform(
            get("/users/with-devices"),
        )
            .andExpect(status().isOk)
            .andExpect(content().json(userDtoJsonTesterList.write(usersWithDevices).json))
    }
}
