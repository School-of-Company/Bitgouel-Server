package team.msg.domain.club

import team.msg.domain.club.model.Club
import team.msg.domain.school.model.School

fun createClub(
    clubId: Long,
    school: School,
    name: String
) = Club(
    id = clubId,
    school = school,
    name = name
)