package team.msg.domain.school

import team.msg.domain.school.enums.HighSchool
import team.msg.domain.school.model.School

fun createSchool(
    schoolId: Long,
    highSchool: HighSchool
) = School(
    id = schoolId,
    highSchool = highSchool
)