package spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.auth.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class MemberOtpRepository (

    private val redisTemplate: RedisTemplate<String, String>
) {

    private val memberEntityName = "member"
    private val memberOtpEntityName = "member_otp"

    fun addOtpById(id: String, otp: String) {

        this.addMemberById(id)
        redisTemplate.opsForValue()
            .set("${memberOtpEntityName}:${id}", otp, Duration.ofMinutes(10))
    }


    fun getOtpById(id: String): String? =
        redisTemplate.opsForValue().get("${memberOtpEntityName}:${id}")


    val memberIds: Set<String>
        get() = redisTemplate.opsForSet().members(memberEntityName)!!


    fun updateAllOtps(newOtps: MutableMap<String, String>) {

        for (each in newOtps.keys) {
            if (this.existsAsOtpById(each))
                redisTemplate.opsForValue().set(
                    "${memberOtpEntityName}:${each}", newOtps[each]!!
                )
            else
                this.removeMemberFromSetById(each)
        }
    }


    fun removeOtpById(id: String) {
        redisTemplate.delete("${memberOtpEntityName}:${id}")
        this.removeMemberFromSetById(id)
    }


    private fun removeMemberFromSetById(id: String) =
        redisTemplate.opsForSet().remove(memberEntityName, id)


    private fun existsAsOtpById(id: String): Boolean =
        redisTemplate.opsForValue().get("${memberOtpEntityName}:${id}") != null


    private fun addMemberById(id: String) {
        redisTemplate.opsForSet()
            .add(memberEntityName, id)
    }


}