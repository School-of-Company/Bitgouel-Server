package team.msg.domain.inquiry.service

import org.springframework.stereotype.Service
import team.msg.common.util.UserUtil
import team.msg.domain.inquiry.model.Inquiry
import team.msg.domain.inquiry.presentation.request.CreateInquiryRequest
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
        val userId = userUtil.queryCurrentUserId()

        if(!userRepository.existsById(userId))
            throw UserNotFoundException("존재하지 않는 유저입니다. : [ id = $userId ]")

        val inquiry = Inquiry(
            id = UUID.randomUUID(),
            userId = userId,
            question = request.question
        )

        inquiryRepository.save(inquiry)
    }
}