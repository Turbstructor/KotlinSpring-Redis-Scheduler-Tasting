package spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.auth.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration
import kotlin.random.Random

@Repository
class MemberOtpRepository (

    private val redisTemplate: RedisTemplate<String, String>
) {

    fun addOtpByUsername(username: String, otp: String) {

        addUsername(username)
        redisTemplate.opsForValue()
            .set("member_otp:$username", otp, Duration.ofMinutes(10))
    }


    private fun addUsername(username: String) {
        redisTemplate.opsForSet()
            .add("members", username)
    }


    fun getMemberOtpByUsername(username: String): String? =
        redisTemplate.opsForValue().get("member_otp:$username")


    fun getMembersList(): List<String> =
        redisTemplate.opsForSet().members("members")!!.toList()


    fun refreshAllOtps(otps: MutableMap<String, String>) {

        redisTemplate.opsForValue().multiSet(otps)

        // Remove obsolete members from 'members' since they need to re-issue otp
        for (each in (otps.keys - getMembersList().toMutableSet()))
            redisTemplate.opsForSet().pop(each)

        redisTemplate.opsForValue().multiSetIfAbsent(otps)
    }


    fun removeOtpByUsername(username: String) {
        redisTemplate.delete("member_otp:$username")
    }


    private fun removeMemberByUsername(username: String) {
        redisTemplate.opsForSet().pop("member_otp:$username")
    }
}