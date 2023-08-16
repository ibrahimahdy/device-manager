package device.manager.user

import device.manager.device.Device
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface UserRepository : CrudRepository<User, UUID> {
    fun findFirstByDevice(device: Device): User?

    @Query("SELECT u FROM User u WHERE u.device IS NOT NULL")
    fun findUsersWithDevices(): List<User>
}
