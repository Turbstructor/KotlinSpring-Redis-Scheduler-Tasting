package spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.auth.service

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.auth.dto.request.SignInPrimaryRequest
import spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.auth.dto.request.SignInSecondaryRequest
import spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.auth.dto.request.SignUpRequest
import spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.auth.dto.response.MemberOtpResponse
import spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.member.dto.response.MemberResponse
import spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.member.entity.Member
import spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.member.entity.from
import spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.member.repository.MemberRepository

@Service
class AuthService (

    private val memberRepository: MemberRepository
) {

    fun signUp(request: SignUpRequest): MemberResponse {

        if (memberRepository.existsByUsername(request.username))
            throw IllegalArgumentException("Member already exists with given username")

        return MemberResponse.from(
            memberRepository.save(
                Member.from (request)
            )
        )
    }

    fun signInPrimary(request: SignInPrimaryRequest): MemberOtpResponse {

        val targetMember = memberRepository.findByUsername(request.username)
            ?: throw IllegalArgumentException("Member does not exist")

        if(request.password != targetMember.password)
            throw IllegalArgumentException("Password does not match")

        // TODO: Implement otp creation here

        return MemberOtpResponse(
            username = targetMember.username,
            otp = ""
        )
    }

    fun signInSecondary(request: SignInSecondaryRequest): String {
        return ""
    }
}