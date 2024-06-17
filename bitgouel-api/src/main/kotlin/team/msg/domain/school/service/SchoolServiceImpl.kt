package team.msg.domain.school.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.domain.club.presentation.data.response.ClubResponse
import team.msg.domain.club.repository.ClubRepository
import team.msg.domain.school.presentation.data.response.SchoolResponse
import team.msg.domain.school.presentation.data.response.SchoolsResponse
import team.msg.domain.school.repository.SchoolRepository

@Service
class SchoolServiceImpl(
    private val schoolRepository: SchoolRepository,
    private val clubRepository: ClubRepository
) : SchoolService {

    /**
     * 학교를 전체 조회하는 비즈니스 로직
     * @return 학교의 정보를 담은 dto
     */
    @Transactional(readOnly = true)
    override fun querySchools(): SchoolsResponse {
        val schools = schoolRepository.findAll()

        val response = SchoolsResponse(
            schools = schools.map {
                val clubs = clubRepository.findAllBySchool(it)
                SchoolResponse(
                    id = it.id,
                    schoolName = it.highSchool,
                    clubs = ClubResponse.schoolOf(clubs)
                )
            }
        )

        return response
    }
}