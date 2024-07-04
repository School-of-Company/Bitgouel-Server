package team.msg.thirdparty.feign.kakao.client.data.res

/**
 * @param addressName 전체 지번 주소 또는 전체 도로명 주소, 입력에 따라 결정됨
 * @param addressType address_name의 값의 타입(Type)
 *  다음 중 하나:
 *  REGION(지명)
 *  ROAD(도로명)
 *  REGION_ADDR(지번 주소)
 *  ROAD_ADDR(도로명 주소)
 * @param x X 좌표값, 경위도인 경우 경도(longitude)
 * @param y Y 좌표값, 경위도인 경우 위도(latitude)
 * @param address 지번 주소 상세 정보
 * @param roadAddress 도로명 주소 상세 정보
 */
data class Document(
    val addressName: String,
    val addressType: String,
    val x: String,
    val y: String,
    val address: Address?,
    val roadAddress: RoadAddress?
)
