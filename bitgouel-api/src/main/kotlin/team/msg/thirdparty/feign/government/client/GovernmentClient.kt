package team.msg.thirdparty.feign.government.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import team.msg.domain.government.presentation.response.GovernmentsResponse

@FeignClient(value = "government", url = "http://localhost:8080/government")
interface GovernmentClient {

    @GetMapping
    fun queryGovernments(): GovernmentsResponse

}