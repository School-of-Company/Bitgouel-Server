package team.msg.domain.map.presentstion.res

class MapResponseData {
    companion object {
        fun of(data: Pair<String, String>) = GetCoordinateResponseData(
            x = data.first,
            y = data.second
        )
    }
}

data class GetCoordinateResponseData(
    val x: String,
    val y: String
)