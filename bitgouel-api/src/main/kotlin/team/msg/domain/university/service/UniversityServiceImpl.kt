package team.msg.domain.university.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.domain.university.model.University
import team.msg.domain.university.presentation.data.request.CreateUniversityRequest
import team.msg.domain.university.presentation.data.response.UniversitiesResponse
import team.msg.domain.university.presentation.data.response.UniversityResponse
import team.msg.domain.university.repository.UniversityRepository

@Service
class UniversityServiceImpl(
    private val universityRepository: UniversityRepository
) : UniversityService {

    /**
     * 대학을 생성하는 비지니스 로직입니다.
     * 같은 이름의 대학이 이미 존재하면 예외를 반환합니다.
     *
     * @param request 생성할 대학의 정보
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun createUniversity(request: CreateUniversityRequest) {
        val university = University(
            department = request.department,
            name = request.universityName
        )

        universityRepository.save(university)
    }

    /**
     *  대학 리스트를 반환하는 비지니스 로직입니다.
     *  @return 대학 리스트
     */
    override fun queryUniversities(): UniversitiesResponse {
        val universities = universityRepository.findAll()

        val response = UniversitiesResponse(
            UniversityResponse.listOf(universities)
        )

        return response
    }
}