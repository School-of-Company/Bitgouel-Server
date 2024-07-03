package team.msg.domain.map.presentation.response

class MapResponse {
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