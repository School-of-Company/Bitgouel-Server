package team.msg.common.util

import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class ParamUtil {

    companion object {

        private val format = DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm:ss-z")

        fun strDateTimeToLocalDateTime(strDateTime: String): LocalDateTime {
            try {
                val zonedDateTime = ZonedDateTime.parse(strDateTime, format)
                return zonedDateTime.toLocalDateTime()
            } catch (e: DateTimeParseException) {
                throw IllegalArgumentException("유효하지 않은 format의 Date: $strDateTime", e)
            }
        }
    }
}