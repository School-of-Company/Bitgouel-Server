package team.msg.domain.university.service

import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.domain.university.exception.AlreadyExistUniversityException
import team.msg.domain.university.exception.UniversityHasProfessorConstraintException
import team.msg.domain.university.exception.UniversityNotFoundException
import team.msg.domain.university.model.University
import team.msg.domain.university.presentation.data.request.CreateDepartmentRequest
import team.msg.domain.university.presentation.data.request.CreateUniversityRequest
import team.msg.domain.university.presentation.data.request.UpdateUniversityRequest
import team.msg.domain.university.presentation.data.response.UniversitiesResponse
import team.msg.domain.university.presentation.data.response.UniversityResponse
import team.msg.domain.university.repository.ProfessorRepository
import team.msg.domain.university.repository.UniversityRepository

@Service
class UniversityServiceImpl(
    private val universityRepository: UniversityRepository,
    private val professorRepository: ProfessorRepository
) : UniversityService {

    /**
     * 대학을 생성하는 비지니스 로직입니다.
     * 같은 이름의 대학이 이미 존재하면 예외를 반환합니다.
     *
     * @param request 생성할 대학의 정보
     */
    @Transactional(rollbackFor = [Exception::class])
    @CacheEvict(value = ["queryUniversities"], allEntries = true)
    override fun createUniversity(request: CreateUniversityRequest) {
        if(universityRepository.existsByName(request.universityName))
            throw AlreadyExistUniversityException("이미 존재하는 대학입니다. info : [ universityName = ${request.universityName} ]")

        val university = University(
            name = request.universityName
        )

        universityRepository.save(university)
    }

    /**
     * 대학을 수정하는 비지니스 로직입니다.
     *
     * @param id 수정할 대학의 식별자
     * @param request 수정할 대학의 정보
     */
    @Transactional(rollbackFor = [Exception::class])
    @CacheEvict(value = ["queryUniversities"], allEntries = true)
    override fun updateUniversity(id: Long, request: UpdateUniversityRequest) {
        if(universityRepository.existsByName(request.universityName))
            throw AlreadyExistUniversityException("이미 존재하는 대학입니다. info : [ universityName = ${request.universityName} ]")

        val university = universityRepository.findByIdOrNull(id)
            ?: throw UniversityNotFoundException("존재하지 않는 대학입니다. info : [ universityId = $id ]")

        val updatedUniversity = University(
            id = university.id,
            name = request.universityName,
            departments = university.departments
        )

        universityRepository.save(updatedUniversity)
    }

    /**
     * 대학을 삭제하는 비지니스 로직입니다.
     * 삭제할 대학과 연관된 대학교수 엔티티가 존재한다면 예외를 반환합나다.
     *
     * @param id 삭제할 대학의 id
     */
    @Transactional(rollbackFor = [Exception::class])
    @CacheEvict(value = ["queryUniversities"], allEntries = true)
    override fun deleteUniversity(id: Long) {
        val university = universityRepository.findByIdOrNull(id)
            ?: throw UniversityNotFoundException("존재하지 않는 대학입니다. info : [ universityId = $id ]")

        if(professorRepository.existsByUniversity(university))
            throw UniversityHasProfessorConstraintException("아직 대학교수가 존재하는 대학입니다. info : [ universityId = $id ]")

        universityRepository.delete(university)
    }

    /**
     *  대학 리스트를 반환하는 비지니스 로직입니다.
     *
     *  @return 대학 리스트
     */
    @Transactional(readOnly = true)
    @Cacheable(value = ["queryUniversities"])
    override fun queryUniversities(): UniversitiesResponse {
        val universities = universityRepository.findAll()

        val response = UniversitiesResponse(
            UniversityResponse.listOf(universities)
        )

        return response
    }

    /**
     * 학과를 추가하는 비지니스 로직입니다.
     *
     * @param id 학과를 추가할 대학의 식별자
     * @param request 추가할 학과의 정보
     */
    @Transactional(rollbackFor = [Exception::class])
    @CacheEvict(value = ["queryUniversities"], allEntries = true)
    override fun createDepartment(id: Long, request: CreateDepartmentRequest) {
        val university = universityRepository.findByIdOrNull(id)
            ?: throw UniversityNotFoundException("존재하지 않는 대학입니다. info : [ universityId = $id ]")

        val departments = buildList {
            addAll(university.departments)
            add(request.department)
        }

        val updatedUniversity = University(
            id = university.id,
            name = university.name,
            departments = departments
        )

        universityRepository.save(updatedUniversity)
    }

    /**
     * 학과를 삭제하는 비지니스 로직입니다.
     *
     * @param id 학과를 삭제할 대학의 식별자
     * @param request 삭제할 학과의 정보
     */
    @Transactional(rollbackFor = [Exception::class])
    @CacheEvict(value = ["queryUniversities"], allEntries = true)
    override fun deleteDepartment(id: Long, department: String) {
        val university = universityRepository.findByIdOrNull(id)
            ?: throw UniversityNotFoundException("존재하지 않는 대학입니다. info : [ universityId = $id ]")

        val updateUniversity = University(
            id = university.id,
            name = university.name,
            departments = university.departments - department
        )

        universityRepository.save(updateUniversity)
    }
}