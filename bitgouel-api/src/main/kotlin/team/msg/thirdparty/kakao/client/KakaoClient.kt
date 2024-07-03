package team.msg.thirdparty.kakao.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import team.msg.thirdparty.kakao.client.data.res.KakaoAddressResponseData
import team.msg.thirdparty.kakao.config.KakaoFeignConfig

@FeignClient(
    name = "KakaoClient",
    url = "https://dapi.kakao.com",
    configuration = [KakaoFeignConfig::class]
)
interface KakaoClient {
    @GetMapping(
        value = ["/v2/local/search/address.json"],
        consumes = ["application/json;charset=UTF-8"]
    )
    fun getKakaoMapAddress(
        @RequestParam("query") query: String,
        @RequestParam("analyze_type") analyzeType: String = "exact"
    ): KakaoAddressResponseData
}