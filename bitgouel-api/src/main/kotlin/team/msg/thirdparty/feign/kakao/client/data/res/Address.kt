package team.msg.thirdparty.feign.kakao.client.data.res

/**
 * @param addressName 전체 지번 주소
 * @param region1DepthName 지역 1 Depth, 시도 단위
 * @param region2DepthName 지역 2 Depth, 구 단위
 * @param region3DepthName 지역 3 Depth, 동 단위
 * @param region3DepthHName 지역 3 Depth, 행정동 명칭
 * @param hCode 행정 코드
 * @param bCode 법정 코드
 * @param mountainYn 산 여부, Y 또는 N
 * @param mainAddressNo 지번 주번지
 * @param subAddressNo 지번 부번지, 없을 경우 빈 문자열("") 반환
 * @param x X 좌표값, 경위도인 경우 경도(longitude)
 * @param y Y 좌표값, 경위도인 경우 위도(latitude)
 */
data class Address(
    val addressName: String,
    val region1DepthName: String,
    val region2DepthName: String,
    val region3DepthName: String,
    val region3DepthHName: String,
    val hCode: String,
    val bCode: String,
    val mountainYn: String,
    val mainAddressNo: String,
    val subAddressNo: String,
    val x: String,
    val y: String
)
