package team.msg.domain.inquiry.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.common.util.UserUtil
import team.msg.domain.inquiry.enums.AnswerStatus
import team.msg.domain.inquiry.model.Inquiry
import team.msg.domain.inquiry.presentation.request.CreateInquiryRequest
import team.msg.domain.inquiry.presentation.response.InquiryResponse
import team.msg.domain.inquiry.presentation.response.InquiryResponses
import team.msg.domain.inquiry.repository.InquiryRepository
import team.msg.domain.user.exception.UserNotFoundException
import team.msg.domain.user.repository.UserRepository
import java.util.*

@Service
class InquiryServiceImpl(
    private val userUtil: UserUtil,
    private val inquiryRepository: InquiryRepository
) : InquiryService {

    /**
     * 문의 사항을 등록하는 비즈니스 로직입니다.
     * @param question이 담겨있는 request
     * @return Unit
     */
    @Transactional
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

        val inquiries = inquiryRepository.findByUser(currentUser)

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
}