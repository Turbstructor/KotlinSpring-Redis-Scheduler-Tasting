package spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.member.dto.response

import spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator.domain.member.entity.Member

data class MemberResponse(

    val username: String
) {

    companion object {

        fun from(member: Member): MemberResponse =
            MemberResponse (
                username = member.username
            )
    }
}
