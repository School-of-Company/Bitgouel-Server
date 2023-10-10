package bitgouel.team.msg.global.error.exception

open class BitgouelException(
    override val message: String,
    val status: Int
) : RuntimeException(message)