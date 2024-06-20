package team.msg.domain.school.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.domain.club.presentation.data.response.ClubResponse
import team.msg.domain.club.repository.ClubRepository
import team.msg.domain.school.exception.AlreadyExistSchoolException
import team.msg.domain.school.model.School
import team.msg.domain.school.presentation.data.request.CreateSchoolRequest
import team.msg.domain.school.presentation.data.response.SchoolResponse
import team.msg.domain.school.presentation.data.response.SchoolsResponse
import team.msg.domain.school.repository.SchoolRepository
import team.msg.thirdparty.aws.s3.AwsS3Util
import java.util.UUID

@Service
class SchoolServiceImpl(
    private val schoolRepository: SchoolRepository,
    private val clubRepository: ClubRepository,
    private val awsS3Util: AwsS3Util
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
                    schoolName = it.name,
                    clubs = ClubResponse.schoolOf(clubs)
                )
            }
        )

        return response
    }

    /**
     * 학교를 생성하는 비지니스 로직
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun createSchool(request: CreateSchoolRequest) {
        val schools = schoolRepository.findAll()

        schools.forEach { school ->
            if (school.name == request.schoolName)
                throw AlreadyExistSchoolException("이미 존재하는 학교입니다. info [ schoolName = ${school.name} ]")
        }

        val imageName = awsS3Util.uploadImage(request.logoImage, UUID.randomUUID().toString())

        val school = School(
            logoImageUrl = imageName,
            name = request.schoolName
        )

        schoolRepository.save(school)
    }
}