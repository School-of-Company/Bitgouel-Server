package team.msg.thirdparty.feign.kakao

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import team.msg.thirdparty.feign.kakao.client.data.res.KakaoAddressResponseData
import team.msg.thirdparty.feign.kakao.config.KakaoFeignConfig

@FeignClient(
    name = "KakaoClient",
    url = "https://dapi.kakao.com",
    configuration = [KakaoFeignConfig::class]
)
interface KakaoClient {
    /**
     * @param query 조회할 도로명/지번 질의어
     * @param analyzeType 검색 결과 제공 방식
     *  similar: 입력한 건물명과 일부만 매칭될 경우에도 확장된 검색 결과 제공, 미지정 시 similar가 적용됨
     *  exact: 주소의 정확한 건물명이 입력된 주소패턴일 경우에 한해, 입력한 건물명과 정확히 일치하는 검색 결과 제공
     */
    @GetMapping(
        value = ["/v2/local/search/address.json"],
        consumes = ["application/json;charset=UTF-8"]
    )
    fun getKakaoMapAddress(
        @RequestParam("query") query: String,
        @RequestParam("analyze_type") analyzeType: String = "exact"
    ): KakaoAddressResponseData
}