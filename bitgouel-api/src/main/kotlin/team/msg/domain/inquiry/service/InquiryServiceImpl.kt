package team.msg.domain.inquiry.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.common.util.UserUtil
import team.msg.domain.inquiry.enums.AnswerStatus
import team.msg.domain.inquiry.exception.ForbiddenCommandInquiryException
import team.msg.domain.inquiry.exception.InquiryAnswerNotFoundException
import team.msg.domain.inquiry.exception.InquiryNotFoundException
import team.msg.domain.inquiry.model.Inquiry
import team.msg.domain.inquiry.model.InquiryAnswer
import team.msg.domain.inquiry.presentation.request.CreateInquiryRequest
import team.msg.domain.inquiry.presentation.response.InquiryDetailResponse
import team.msg.domain.inquiry.presentation.response.InquiryResponse
import team.msg.domain.inquiry.presentation.response.InquiryResponses
import team.msg.domain.inquiry.repository.InquiryAnswerRepository
import team.msg.domain.inquiry.repository.InquiryRepository
import team.msg.domain.student.exception.StudentNotFoundException
import team.msg.domain.student.model.Student
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.user.model.User
import java.util.*

@Service
class InquiryServiceImpl(
    private val userUtil: UserUtil,
    private val inquiryRepository: InquiryRepository,
    private val inquiryAnswerRepository: InquiryAnswerRepository
) : InquiryService {

    /**
     * 문의 사항을 등록하는 비즈니스 로직입니다.
     * @param question이 담겨있는 request
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun createInquiry(request: CreateInquiryRequest) {
        val currentUser = userUtil.queryCurrentUser()

        val inquiry = Inquiry(
            id = UUID.randomUUID(),
            user = currentUser,
            question = request.question,
            questionDetail = request.questionDetail,
            answerStatus = AnswerStatus.UNANSWERED
        )

        inquiryRepository.save(inquiry)
    }

    /**
     * 유저가 자신이 등록한 문의 사항을 조회하는 비즈니스 로직입니다.
     * @return 자신이 등록한 문의사항 response
     */
    @Transactional(readOnly = true)
    override fun queryMyInquiries(): InquiryResponses {
        val currentUser = userUtil.queryCurrentUser()

        val inquiries = inquiryRepository.findAllByUser(currentUser)

        return InquiryResponse.listOf(inquiries)
    }

    /**
     * 전체 문의 사항을 조회하는 비즈니스 로직입니다.
     * 답변 상태, 키워드를 통해 필터링 합니다.
     * @param 답변 여부 상태, 키워드
     * @return 자신이 등록한 문의사항 response
     */
    @Transactional(readOnly = true)
    override fun queryAllInquiries(answerStatus: AnswerStatus?, keyword: String): InquiryResponses {

        val inquiries = inquiryRepository.search(answerStatus,keyword)

        return InquiryResponse.listOf(inquiries)
    }

    /**
     * 자신이 등록한 문의사항의 상세 정보를 조회하는 비즈니스 로직입니다.
     * @param 문의사항 id
     * @return 자신이 등록한 문의사항 detail
     */
    @Transactional(readOnly = true)
    override fun queryInquiryDetail(id: UUID): InquiryDetailResponse {
        val inquiry = inquiryRepository findById id

        val inquiryAnswer = if(inquiry.answerStatus == AnswerStatus.ANSWERED) {
            inquiryAnswerRepository findByInquiryId inquiry.id
        } else null

        return InquiryResponse.detailOf(inquiry, inquiryAnswer)
    }

    /**
     * 자신이 등록한 문의사항을 삭제하는 비즈니스 로직입니다.
     * 답변이 있다면 답변까지 함께 삭제합니다.
     * @param 문의사항 id
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun deleteInquiry(id: UUID) {
        val currentUser = userUtil.queryCurrentUser()

        val inquiry = inquiryRepository findById id

        if(currentUser != inquiry.user)
            throw ForbiddenCommandInquiryException("문의사항을 삭제할 권한이 없습니다. info : [ userId = ${currentUser.id}, inquiryId = $id ]")

        if(inquiry.answerStatus == AnswerStatus.ANSWERED) {
            val inquiryAnswer =  inquiryAnswerRepository findByInquiryId id
            inquiryAnswerRepository.delete(inquiryAnswer)
        }

        inquiryRepository.deleteById(id)
    }

    /**
     * 문의사항을 삭제하는 어드민 비즈니스 로직입니다.
     * 답변이 있다면 답변까지 함께 삭제합니다.
     * @param 문의사항 id
     */
    override fun rejectInquiry(id: UUID) {
        val inquiry = inquiryRepository findById id

        if(inquiry.answerStatus == AnswerStatus.ANSWERED) {
            val inquiryAnswer = inquiryAnswerRepository findByInquiryId id
            inquiryAnswerRepository.delete(inquiryAnswer)
        }

        inquiryRepository.deleteById(id)
    }

    private infix fun InquiryRepository.findById(id: UUID): Inquiry =
        this.findByIdOrNull(id)
            ?: throw InquiryNotFoundException("존재하지 않는 문의사항입니다. info : [ inquiryId = $id ]")

    private infix fun InquiryAnswerRepository.findByInquiryId(inquiryId: UUID): InquiryAnswer =
        this.findByInquiryId(inquiryId)
            ?: throw InquiryAnswerNotFoundException("존재하지 않는 문의사항의 답변입니다. info : [ inquiryId = $inquiryId ]")

}
