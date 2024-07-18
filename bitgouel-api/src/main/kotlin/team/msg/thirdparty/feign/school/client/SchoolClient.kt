package team.msg.thirdparty.feign.school.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import team.msg.domain.school.presentation.data.response.SchoolNamesResponse
import team.msg.domain.school.presentation.data.response.SchoolsResponse

@FeignClient(value = "school", url = "http://localhost:8080/school")
interface SchoolClient {

    @GetMapping
    fun querySchools(): SchoolsResponse

    @GetMapping("/name")
    fun querySchoolNames(): SchoolNamesResponse

}