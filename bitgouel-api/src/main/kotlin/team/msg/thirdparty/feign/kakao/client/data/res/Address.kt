package team.msg.thirdparty.feign.kakao.client.data.res

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * @param addressName 전체 지번 주소
 * @param hCode 행정 코드
 * @param bCode 법정 코드
 * @param mountainYn 산 여부, Y 또는 N
 * @param mainAddressNo 지번 주번지
 * @param subAddressNo 지번 부번지, 없을 경우 빈 문자열("") 반환
 * @param x X 좌표값, 경위도인 경우 경도(longitude)
 * @param y Y 좌표값, 경위도인 경우 위도(latitude)
 */
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Address(
    val addressName: String,
    val hCode: String,
    val bCode: String,
    val mountainYn: String,
    val mainAddressNo: String,
    val subAddressNo: String,
    val x: String,
    val y: String
)
