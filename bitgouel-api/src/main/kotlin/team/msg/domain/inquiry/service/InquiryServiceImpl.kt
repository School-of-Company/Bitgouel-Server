package team.msg.domain.inquiry.service

import org.springframework.stereotype.Service
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
    private val inquiryRepository: InquiryRepository,
    private val userRepository: UserRepository
) : InquiryService {

    /**
     * 문의 사항을 등록하는 비즈니스 로직입니다.
     * @param question이 담겨있는 request
     * @return Unit
     */
    override fun createInquiry(request: CreateInquiryRequest) {
        val currentUser = userUtil.queryCurrentUser()

        val inquiry = Inquiry(
            id = UUID.randomUUID(),
            user = currentUser,
            question = request.question,
            answerStatus = AnswerStatus.UNANSWERED
        )

        inquiryRepository.save(inquiry)
    }

    override fun queryMyInquiries(): InquiryResponses {
        val currentUser = userUtil.queryCurrentUser()

        val inquiries = inquiryRepository.findByUser(currentUser)

        return InquiryResponse.listOf(inquiries)
    }

    override fun queryAllInquiries(answerStatus: AnswerStatus?, keyword: String): InquiryResponses {
        TODO("Not yet implemented")
    }
}