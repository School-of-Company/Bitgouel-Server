package team.msg.thirdparty.feign.club.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import team.msg.domain.club.presentation.data.response.ClubNamesResponse

@FeignClient(value = "club", url = "http://localhost:8080")
interface ClubClient {

    @GetMapping("/club/name")
    fun queryAllClubNames(@RequestParam("schoolName") schoolName: String?): ClubNamesResponse

}