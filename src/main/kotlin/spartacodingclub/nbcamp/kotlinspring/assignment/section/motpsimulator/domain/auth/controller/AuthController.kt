package spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.auth.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.*
import spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.auth.dto.request.SignInPrimaryRequest
import spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.auth.dto.request.SignInSecondaryRequest
import spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.auth.dto.request.SignUpRequest
import spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.auth.dto.response.MemberOtpResponse
import spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.auth.service.AuthService
import spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.member.dto.response.MemberResponse

@RestController
@RequestMapping("/api/v1/auth")
class AuthController (

    private val authService: AuthService
) {

    @PostMapping("/sign-up")
    fun signUp(@RequestBody request: SignUpRequest): ResponseEntity<MemberResponse> =
        ResponseEntity
            .status(HttpStatus.CREATED)
            .body(authService.signUp(request))

    @PostMapping("/sign-in-primary")
    fun signInPrimary(@RequestBody request: SignInPrimaryRequest): ResponseEntity<MemberOtpResponse> =
        ResponseEntity
            .status(HttpStatus.CREATED)
            .body(authService.signInPrimary(request))

    @PostMapping("/sign-in-secondary")
    fun signInSecondary(@RequestBody request: SignInSecondaryRequest): ResponseEntity<MemberResponse> =
        ResponseEntity
            .status(HttpStatus.OK)
            .body(authService.signInSecondary(request))


    @Scheduled(cron = "0 * * * * *")
    fun refreshOtps() =
        authService.refreshOtps()

}