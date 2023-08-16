package device.manager.device

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface DeviceRepository : CrudRepository<Device, UUID> {
    fun existsBySerialNumber(serialNumber: String): Boolean
}
