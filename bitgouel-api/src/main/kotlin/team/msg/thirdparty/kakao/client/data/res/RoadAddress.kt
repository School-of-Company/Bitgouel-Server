package team.msg.thirdparty.kakao.client.data.res

/**
 * @param addressName 전체 도로명 주소
 * @param region1DepthName 지역명 1
 * @param region2DepthName 지역명 2 Depth
 * @param region3DepthName 지역명 3 Depth
 * @param loadName 도로명
 * @param undergroundYn 지하 여부, Y 또는 N
 * @param mainBuildingNo 건물 본번
 * @param subBuildingNo 건물 부번, 없을 경우 빈 문자열("") 반환
 * @param buildingName 건물 이름
 * @param zoneNo 우편번호
 * @param x X 좌표값, 경위도인 경우 경도(longitude)
 * @param y Y 좌표값, 경위도인 경우 위도(latitude)
 */
data class RoadAddress(
    val addressName: String,
    val region1DepthName: String,
    val region2DepthName: String,
    val region3DepthName: String,
    val loadName: String,
    val undergroundYn: String,
    val mainBuildingNo: String,
    val subBuildingNo: String,
    val buildingName: String,
    val zoneNo: String,
    val x: String,
    val y: String
)
