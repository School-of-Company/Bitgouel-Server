package team.msg.thirdparty.feign.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import team.msg.domain.faq.presentation.data.response.FaqsResponse

@FeignClient(value = "faq", url = "http://localhost:8080")
interface FaqClient {

    @GetMapping("/faq")
    fun queryFaq(): FaqsResponse

}