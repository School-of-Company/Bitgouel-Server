package team.msg.domain.university.presentation.data.response

import team.msg.domain.university.model.University

data class UniversityResponse(
    val id: Long,
    val universityName: String,
    val departments: List<String>
) {
    companion object {

        fun of(university: University) =
            UniversityResponse(
                id = university.id,
                departments = university.departments,
                universityName = university.name
            )


        fun listOf(universities: List<University>) =
            universities.map { of(it) }
    }
}

data class UniversitiesResponse(
    val universities : List<UniversityResponse>
)