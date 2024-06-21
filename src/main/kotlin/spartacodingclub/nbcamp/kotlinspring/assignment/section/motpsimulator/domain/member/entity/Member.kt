package spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.member.entity

import jakarta.persistence.*
import spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.auth.dto.request.SignUpRequest
import java.util.*

@Entity
@Table(name = "member")
class Member (

    @Column(name = "username", unique = true, nullable = false)
    val username: String,

    @Column(name = "password", nullable = false)
    var password: String
) {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null


    companion object {
    }
}

fun Member.Companion.from(request: SignUpRequest) = Member (
    username = request.username,
    password = request.password
)