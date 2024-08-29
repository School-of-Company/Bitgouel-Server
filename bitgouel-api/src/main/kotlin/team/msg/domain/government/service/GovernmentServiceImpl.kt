package team.msg.domain.government.service

import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.domain.government.exception.AlreadyExistGovernmentException
import team.msg.domain.government.exception.GovernmentHasGovernmentInstructorConstraintException
import team.msg.domain.government.exception.GovernmentNotFoundException
import team.msg.domain.government.model.Government
import team.msg.domain.government.presentation.request.CreateGovernmentRequestData
import team.msg.domain.government.presentation.response.GovernmentResponse
import team.msg.domain.government.presentation.response.GovernmentsResponse
import team.msg.domain.government.repository.GovernmentInstructorRepository
import team.msg.domain.government.repository.GovernmentRepository

@Service
class GovernmentServiceImpl(
    private val governmentRepository: GovernmentRepository,
    private val governmentInstructorRepository: GovernmentInstructorRepository
) : GovernmentService {

    /**
     * 유관기관을 생성하는 비지니스 로직입니다.
     * 같은 이름의 유관기관이 이미 존재하면 예외를 반환합니다.
     *
     * @param request 생성할 유관기관의 정보
     */
    @Transactional(rollbackFor = [Exception::class])
    @CacheEvict(value = ["queryGovernments"], allEntries = true)
    override fun createGovernment(request: CreateGovernmentRequestData) {
        if(governmentRepository.existsByName(request.governmentName))
            throw AlreadyExistGovernmentException("이미 존재하는 유관기관입니다. info : [ governmentName = ${request.governmentName} ]")

        val government = Government(
            field = request.field,
            name = request.governmentName
        )

        governmentRepository.save(government)
    }

    /**
     * 유관기관 리스트를 반환하는 비지니스 로직입니다.
     * @return 유관기관 리스트
     */
    @Transactional(readOnly = true)
    @Cacheable(value = ["queryGovernments"])
    override fun queryGovernments(): GovernmentsResponse {
        val governments = governmentRepository.findAll()
        val response = GovernmentsResponse(
            governments = governments
                .map {
                    GovernmentResponse.of(it)
                }
        )

        return response
    }

    /**
     * 유관기관을 삭제하는 비지니스로직입니다.
     * @param id 삭제할 유관기관의 id
     */
    @Transactional(rollbackFor = [Exception::class])
    @CacheEvict(value = ["queryGovernments"], allEntries = true)
    override fun deleteGovernment(id: Long) {
        val government = governmentRepository.findByIdOrNull(id)
            ?: throw GovernmentNotFoundException("존재하지 않는 유관기관입니다. info : [ governmentId = $id ]")

        if(governmentInstructorRepository.existsByGovernment(government))
            throw GovernmentHasGovernmentInstructorConstraintException("아직 유관기관 강사가 존재하는 유관기관입니다. info : [ government = $id ]")

        governmentRepository.delete(government)
    }

}