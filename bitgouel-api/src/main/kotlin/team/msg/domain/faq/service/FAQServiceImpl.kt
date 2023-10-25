package team.msg.domain.faq.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.common.util.UserUtil
import team.msg.domain.admin.exception.AdminNotFoundException
import team.msg.domain.admin.repository.AdminRepository
import team.msg.domain.faq.exception.FaqNotFoundException
import team.msg.domain.faq.model.Faq
import team.msg.domain.faq.repository.FaqRepository
import team.msg.domain.faq.presentation.data.request.CreateFaqRequest
import team.msg.domain.faq.presentation.data.response.QueryAllFaqsResponse
import team.msg.domain.faq.presentation.data.response.QueryFaqDetailsResponse

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
        val admin = adminRepository.findByUser(user) ?: throw AdminNotFoundException("존재하지 않는 어드민입니다. info : [ userId = ${user.id} ]")

        val faq = Faq(
            question = createFaqRequest.question,
            answer = createFaqRequest.answer,
            admin = admin
        )

        faqRepository.save(faq)
    }

    @Transactional(rollbackFor = [Exception::class], readOnly = true)
    override fun queryAllFaqs(): List<QueryAllFaqsResponse> {
        val faqs = faqRepository.findAll()

        val response = faqs.map {
            QueryAllFaqsResponse(
                id = it.id,
                question = it.question
            )
        }

        return response
    }

    @Transactional(rollbackFor = [Exception::class], readOnly = true)
    override fun queryFaqDetails(id: Long): QueryFaqDetailsResponse {
        val faq = faqRepository.findByIdOrNull(id) ?: throw FaqNotFoundException("존재하지 않는 faq 입니다. info : [ faqId = $id ]")

        return QueryFaqDetailsResponse(
            id = faq.id,
            question = faq.question,
            answer = faq.answer
        )
    }
}