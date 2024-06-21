package spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.auth.dto.request

data class SignInSecondaryRequest(

    val username: String,
    val otp: String
)
