package device.manager.user

import device.manager.device.Device
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import jakarta.validation.constraints.NotBlank
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(
    name = "app_user",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["firstName", "lastName", "address", "birthday"]),
    ],
)
data class User(
    @Id
    @GeneratedValue
    val id: UUID = UUID.randomUUID(),

    @field:NotBlank
    val firstName: String,

    @field:NotBlank
    val lastName: String,

    val address: String,

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val birthday: LocalDate,

    @OneToOne(cascade = [CascadeType.ALL])
    var device: Device? = null
) {
    fun toDto() = UserDto(id, firstName, lastName, address, birthday, device?.toDto())
}
