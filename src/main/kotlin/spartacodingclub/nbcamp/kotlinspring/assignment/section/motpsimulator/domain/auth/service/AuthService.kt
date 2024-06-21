package spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.auth.service

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.auth.dto.request.SignInPrimaryRequest
import spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.auth.dto.request.SignInSecondaryRequest
import spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.auth.dto.request.SignUpRequest
import spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.auth.dto.response.MemberOtpResponse
import spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.auth.repository.MemberOtpRepository
import spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.member.dto.response.MemberResponse
import spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.member.entity.Member
import spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.member.entity.from
import spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.member.repository.MemberRepository
import kotlin.random.Random

@Service
class AuthService (

    private val memberRepository: MemberRepository,
    private val memberOtpRepository: MemberOtpRepository
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
        val code = String.format("%06d", Random.nextInt(1000000))
        memberOtpRepository.addOtpByUsername(targetMember.username, code)

        return MemberOtpResponse(
            username = targetMember.username,
            otp = code
        )
    }

    fun signInSecondary(request: SignInSecondaryRequest): String {

        val targetMember = memberRepository.findByUsername(request.username)
            ?: throw IllegalArgumentException("Member does not exist")

        val targetOtp = memberOtpRepository.getMemberOtpByUsername(targetMember.username)
            ?: throw IllegalArgumentException("Member does not exist")

        if (targetOtp != request.otp)
            throw IllegalArgumentException("Otp does not match")

        memberOtpRepository.removeOtpByUsername(targetMember.username)
        return "A u t h o r i z e d"
    }

    fun refreshOtps() {

        val members = memberOtpRepository.getMembersList()
        val otps: MutableMap<String, String> = mutableMapOf()

        for (each in members)
            String.format("%06d", Random.nextInt(1000000)).let {
                otps.put("member_otp:$each", it)
            }

        memberOtpRepository.refreshAllOtps(otps)
    }
}