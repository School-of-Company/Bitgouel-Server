package team.msg.domain.inquiry.service

import org.springframework.stereotype.Service
import team.msg.common.util.UserUtil
import team.msg.domain.inquiry.presentation.web.CreateInquiryRequest
import team.msg.domain.inquiry.repository.InquiryRepository

@Service
class InquiryServiceImpl(
    private val userUtil: UserUtil,
    private val inquiryRepository: InquiryRepository
) : InquiryService {

    override fun createInquiry(request: CreateInquiryRequest) {

    }
}