package spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.auth.service

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

        val code = String.format("%06d",
            Random.nextInt(1000000))
        memberOtpRepository.addOtpById(targetMember.id, code)

        return MemberOtpResponse(
            id = targetMember.id,
            username = targetMember.username,
            otp = code
        )
    }

    fun signInSecondary(request: SignInSecondaryRequest): MemberResponse {

        val targetMember = memberRepository.findByUsername(request.username)
            ?: throw IllegalArgumentException("Member does not exist")
        val targetMemberOtp = memberOtpRepository.getOtpById(targetMember.id)
            ?: throw IllegalArgumentException("Member has to sign in first to get OTP code")

        if (targetMemberOtp != request.otp)
            throw IllegalArgumentException("OTP does not match")

        memberOtpRepository.removeOtpById(targetMember.id)
        return MemberResponse.from(targetMember)
    }

    fun refreshOtps() {

        val memberIds = memberOtpRepository.memberIds
        val newOtps: MutableMap<String, String> = mutableMapOf()

        for (each in memberIds)
            String.format("%06d", Random.nextInt(1000000)).let {
                newOtps.put(each, it)
            }

        memberOtpRepository.updateAllOtps(newOtps)
    }
}