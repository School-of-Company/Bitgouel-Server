package team.msg.domain.university.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.domain.university.model.University
import team.msg.domain.university.presentation.data.request.CreateUniversityRequest
import team.msg.domain.university.repository.UniversityRepository

@Service
class UniversityServiceImpl(
    private val universityRepository: UniversityRepository
) : UniversityService {

    @Transactional(rollbackFor = [Exception::class])
    override fun createUniversity(request: CreateUniversityRequest) {
        val university = University(
            department = request.department,
            name = request.universityName
        )

        universityRepository.save(university)
    }
}