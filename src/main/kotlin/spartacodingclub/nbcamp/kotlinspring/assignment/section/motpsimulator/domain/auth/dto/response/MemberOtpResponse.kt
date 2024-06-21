package spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.auth.dto.response

data class MemberOtpResponse(

    val id: String,
    val username: String,
    val otp: String
)