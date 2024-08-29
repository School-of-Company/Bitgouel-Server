package team.msg.domain.school.service

import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import team.msg.domain.club.presentation.data.response.ClubResponse
import team.msg.domain.club.repository.ClubRepository
import team.msg.domain.school.exception.AlreadyExistSchoolException
import team.msg.domain.school.exception.InvalidExtensionException
import team.msg.domain.school.exception.NotEmptySchoolException
import team.msg.domain.school.exception.SchoolNotFoundException
import team.msg.domain.school.model.School
import team.msg.domain.school.presentation.data.request.CreateSchoolRequest
import team.msg.domain.school.presentation.data.request.UpdateSchoolRequest
import team.msg.domain.school.presentation.data.response.SchoolNameResponse
import team.msg.domain.school.presentation.data.response.SchoolNamesResponse
import team.msg.domain.school.presentation.data.response.SchoolResponse
import team.msg.domain.school.presentation.data.response.SchoolsResponse
import team.msg.domain.school.repository.SchoolRepository
import team.msg.thirdparty.aws.s3.AwsS3Util
import java.util.*

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
    @Cacheable(value = ["querySchools"])
    override fun querySchools(): SchoolsResponse {
        val schools = schoolRepository.findAll()

        val response = SchoolsResponse(
            schools = schools.map {
                val clubs = clubRepository.findAllBySchool(it)
                SchoolResponse(
                    id = it.id,
                    name = it.name,
                    line = it.line,
                    departments = it.departments,
                    logoImageUrl = it.logoImageUrl,
                    clubs = ClubResponse.schoolOf(clubs)
                )
            }
        )

        return response
    }

    /**
     * 학교의 상세 정보를 조회하는 비지니스 로직
     * @param id 학교 id
     * @return 학교 정보를 담은 dto
     */
    @Transactional(readOnly = true)
    override fun querySchoolDetails(id: Long): SchoolResponse {
        val school = schoolRepository.findByIdOrNull(id)
            ?: throw SchoolNotFoundException("존재하지 않는 학교입니다. info [ schoolId = $id ]")

        val clubs = clubRepository.findAllBySchool(school)

        val response = school.run {
            SchoolResponse(
                id = id,
                name = name,
                line = line,
                departments = departments,
                logoImageUrl = logoImageUrl,
                clubs = ClubResponse.schoolOf(clubs)
            )
        }

        return response
    }

    /**
     * 학교 이름을 전체 조회하는 비즈니스 로직
     * @return 학교 이름을 담은 dto
     */
    @Transactional(readOnly = true)
    override fun querySchoolNames(): SchoolNamesResponse {
        val schools = schoolRepository.findAll()

        val response = SchoolNamesResponse(
            schools = schools.map {
                SchoolNameResponse(
                    name = it.name
                )
            }
        )

        return response
    }

    /**
     * 학교를 생성하는 비지니스 로직
     * @param 생성할 학교의 정보
     */
    @Transactional(rollbackFor = [Exception::class])
    @CacheEvict(value = ["querySchools"], allEntries = true)
    override fun createSchool(request: CreateSchoolRequest, logoImage: MultipartFile) {
        if (schoolRepository.existsByName(request.schoolName)) {
            throw AlreadyExistSchoolException("이미 존재하는 학교입니다. info [ schoolName = ${request.schoolName} ]")
        }

        val imageUrl = awsS3Util.uploadImage(logoImage, UUID.randomUUID().toString())

        val school = School(
            logoImageUrl = imageUrl,
            name = request.schoolName,
            line = request.line,
            departments = request.departments
        )

        schoolRepository.save(school)
    }

    /**
     * 학교를 수정하는 비지니스 로직
     * @param 수정할 학교의 id와 수정할 내용들
     */
    @Transactional(rollbackFor = [Exception::class])
    @CacheEvict(value = ["querySchools"], allEntries = true)
    override fun updateSchool(id: Long, request: UpdateSchoolRequest, logoImage: MultipartFile) {
        if (schoolRepository.existsByNameAndIdNotLike(request.schoolName, id)) {
            throw AlreadyExistSchoolException("이미 존재하는 학교입니다. info : [ schoolName = ${request.schoolName} ]")
        }

        val school = schoolRepository.findByIdOrNull(id)
            ?: throw SchoolNotFoundException("존재하지 않는 학교입니다. info : [ schoolId = $id ]")

        val logoImageUrl = if(logoImage.size == 0L) {
            school.logoImageUrl
        } else {
            if (logoImage.contentType !in listOf(JPEG, JPG, PNG, HEIC)) {
                throw InvalidExtensionException("유효하지 않은 확장자입니다. info : [ contentType = ${logoImage.contentType} ]")
            }

            awsS3Util.deleteImage(school.logoImageUrl)
            awsS3Util.uploadImage(logoImage, UUID.randomUUID().toString())
        }

        val updateSchool = School(
            id = id,
            logoImageUrl = logoImageUrl,
            name = request.schoolName,
            line = request.line,
            departments = request.departments
        )
        schoolRepository.save(updateSchool)
    }

    /**
     * 학교를 삭제하는 비지니스 로직
     * @param 삭제할 학교의 id
     */
    @Transactional(rollbackFor = [Exception::class])
    @CacheEvict(value = ["querySchools"], allEntries = true)
    override fun deleteSchool(id: Long) {
        val school = schoolRepository.findByIdOrNull(id)
            ?: throw SchoolNotFoundException("존재하지 않는 학교입니다. info [ schoolId = $id ]")

        if (clubRepository.existsBySchool(school))
            throw NotEmptySchoolException("학교에 삭제되지 않은 동아리가 있습니다. info : [ schoolId = $id ]")

        awsS3Util.deleteImage(school.logoImageUrl)
        schoolRepository.deleteById(id)
    }

    companion object {
        const val JPEG = "image/jpeg"
        const val PNG = "image/png"
        const val JPG = "image/jpg"
        const val HEIC = "image/heic"
    }

}