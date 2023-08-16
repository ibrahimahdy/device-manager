package device.manager.user

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.UUID

@Validated
@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @PostMapping
    fun createUser(
        @Valid @RequestBody
        user: UserDto,
    ): ResponseEntity<UserDto> {
        val createdUser = userService.createUser(user)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser)
    }

    @PostMapping("/{userId}/assign-device/{deviceId}")
    fun assignDeviceToUser(
        @PathVariable userId: UUID,
        @PathVariable deviceId: UUID,
    ): ResponseEntity<UserDto> {
        val assignedUser = userService.assignDeviceToUser(userId, deviceId)
        return ResponseEntity.ok(assignedUser)
    }

    @GetMapping("/with-devices")
    fun listUsersWithDevices(): ResponseEntity<List<UserDto>> {
        val usersWithDevices = userService.getUsersWithDevices()
        return ResponseEntity.ok(usersWithDevices)
    }
}
