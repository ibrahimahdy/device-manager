package device.manager.device

import device.manager.user.User
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import jakarta.validation.constraints.NotBlank
import java.util.UUID

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["serialNumber"])])
data class Device(
    @Id
    @GeneratedValue
    val id: UUID = UUID.randomUUID(),

    @field:NotBlank
    val serialNumber: String,

    @field:NotBlank
    val model: String,

    val phoneNumber: String,

    @OneToOne(mappedBy = "device")
    val user: User? = null,
) {
    fun toDto() = DeviceDto(id, serialNumber, model, phoneNumber)
}
