package team.msg.domain.faq.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.common.util.UserUtil
import team.msg.domain.admin.exception.AdminNotFoundException
import team.msg.domain.admin.repository.AdminRepository
import team.msg.domain.fAQ.model.FAQ
import team.msg.domain.fAQ.repository.FAQRepository
import team.msg.domain.faq.presentation.data.request.CreateFAQRequest

@Service
class FAQServiceImpl(
    private val faqRepository: FAQRepository,
    private val userUtil: UserUtil,
    private val adminRepository: AdminRepository
) : FAQService {

    @Transactional(rollbackFor = [Exception::class])
    override fun createFAQ(createFAQRequest: CreateFAQRequest) {
        val user = userUtil.queryCurrentUser()
        val admin = adminRepository.findByUser(user) ?: throw AdminNotFoundException("존재하지 않는 어드민입니다. info : [ userId = ${user.id} ]")

        val faq = FAQ(
            question = createFAQRequest.question,
            answer = createFAQRequest.answer,
            admin = admin
        )

        faqRepository.save(faq)
    }
}