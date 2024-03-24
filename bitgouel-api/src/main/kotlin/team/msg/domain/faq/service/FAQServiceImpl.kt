package team.msg.domain.faq.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.common.util.UserUtil
import team.msg.domain.admin.exception.AdminNotFoundException
import team.msg.domain.admin.repository.AdminRepository
import team.msg.domain.faq.model.Faq
import team.msg.domain.faq.presentation.data.request.CreateFaqRequest
import team.msg.domain.faq.presentation.data.response.FaqResponse
import team.msg.domain.faq.presentation.data.response.FaqsResponse
import team.msg.domain.faq.repository.FaqRepository


@Service
class FaqServiceImpl(
    private val faqRepository: FaqRepository,
    private val userUtil: UserUtil,
    private val adminRepository: AdminRepository
) : FaqService {

    /**
     * FAQ 등록을 처리하는 비지니스 로직입니다.
     * @param FAQ 등록을 처리하기 위한 request dto 입니다.
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun createFaq(createFaqRequest: CreateFaqRequest) {
        val user = userUtil.queryCurrentUser()

        val admin = adminRepository.findByUser(user)
            ?: throw AdminNotFoundException("어드민을 찾을 수 없습니다. info : [ userId = ${user.id} ]")

        val faq = Faq(
            question = createFaqRequest.question,
            answer = createFaqRequest.answer,
            admin = admin
        )

        faqRepository.save(faq)
    }

    /**
    * FAQ 전제 조회를 처리하는 비지니스 로직입니다.
    */
    @Transactional(readOnly = true)
    override fun queryAllFaqs(): FaqsResponse {
        val faqs = faqRepository.findAll()

        val response = FaqsResponse(
            FaqResponse.listOf(faqs)
        )

        return response
    }
}