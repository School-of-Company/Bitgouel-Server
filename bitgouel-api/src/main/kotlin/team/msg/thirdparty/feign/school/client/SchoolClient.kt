package team.msg.thirdparty.feign.school.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import team.msg.domain.school.presentation.data.response.SchoolNamesResponse
import team.msg.domain.school.presentation.data.response.SchoolsResponse

@FeignClient(value = "school", url = "http://localhost:8080")
interface SchoolClient {

    @GetMapping("/school")
    fun querySchools(): SchoolsResponse

    @GetMapping("/school/name")
    fun querySchoolNames(): SchoolNamesResponse

}