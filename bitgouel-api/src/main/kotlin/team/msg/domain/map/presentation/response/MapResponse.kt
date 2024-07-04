package team.msg.domain.map.presentation.response

class MapResponse {
    companion object {
        fun of(data: Pair<String, String>) = GetCoordinateResponse(
            x = data.first,
            y = data.second
        )
    }
}

data class GetCoordinateResponse(
    val x: String,
    val y: String
)