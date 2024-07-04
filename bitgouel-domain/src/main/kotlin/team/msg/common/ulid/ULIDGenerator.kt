package team.msg.common.ulid

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.SecureRandom
import java.time.Instant

/**
 * ULID를 생성하는 ULIDGenerator입니다. ULID는 현재 시간 정보를 기반으로하며
 * 보안 랜덤 값을 사용하여 16바이트의 바이트 배열을 생성합니다. 그런 다음 이를 문자열로 변환하여 ULID를 생성합니다.
 */
class ULIDGenerator private constructor() {
    companion object {
        private val random = SecureRandom()

        fun generateULID(): String {
            val milliseconds = Instant.now().toEpochMilli()

            val bytes = ByteArray(16)
            val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN)
            buffer.putLong(milliseconds)
            buffer.putLong(random.nextLong())

            return bytes.toULIDString()
        }

        private fun ByteArray.toULIDString(): String {
            val sb = StringBuilder()
            forEachIndexed { index,byte ->
                if (index == 6 || index == 8 || index == 10 || index == 12) {
                    sb.append('-')
                }
                sb.append(((byte.toInt() shr 4) and 0xF).toString(16))
                sb.append((byte.toInt() and 0xF).toString(16))
            }
            return sb.toString()
        }
    }
}
