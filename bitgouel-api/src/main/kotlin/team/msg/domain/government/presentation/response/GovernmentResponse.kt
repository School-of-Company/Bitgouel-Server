package team.msg.domain.government.presentation.response

import team.msg.common.enums.Field
import team.msg.domain.government.model.Government

data class GovernmentResponse (
    val id: Long,
    val field: Field,
    val governmentName: String
) {
    fun of(government: Government) = GovernmentResponse(
        id = id,
        field = field,
        governmentName = government.name
    )

    fun listOf(governments: List<Government>) = GovernmentsResponse(
        governments.map {
            of(it)
        }
    )
}

data class GovernmentsResponse(
    val governments: List<GovernmentResponse>
)