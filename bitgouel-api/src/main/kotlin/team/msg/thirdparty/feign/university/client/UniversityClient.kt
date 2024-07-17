package team.msg.thirdparty.feign.university.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import team.msg.domain.university.presentation.data.response.UniversitiesResponse

@FeignClient(value = "university", url = "http://localhost:8080")
interface UniversityClient {

    @GetMapping("/university")
    fun queryUniversities(): UniversitiesResponse

}