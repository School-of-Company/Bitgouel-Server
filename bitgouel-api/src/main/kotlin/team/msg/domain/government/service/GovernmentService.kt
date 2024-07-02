package team.msg.domain.government.service

import team.msg.domain.government.presentation.request.CreateGovernmentRequestData
import team.msg.domain.government.presentation.response.GovernmentsResponse

interface GovernmentService {
    fun createGovernment(request: CreateGovernmentRequestData)
    fun queryGovernments(): GovernmentsResponse
    fun deleteGovernment(id: Long)
}