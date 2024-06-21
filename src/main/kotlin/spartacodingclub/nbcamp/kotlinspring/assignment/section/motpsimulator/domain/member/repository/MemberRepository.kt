package spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.member.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.member.entity.Member
import java.util.UUID

@Repository
interface MemberRepository: JpaRepository<Member, UUID> {

    fun existsByUsername(username: String): Boolean

    fun findByUsername(username: String): Member?
}