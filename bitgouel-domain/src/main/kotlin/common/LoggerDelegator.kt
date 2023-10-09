package common

import org.slf4j.LoggerFactory
import java.util.logging.Logger

class LoggerDelegator {
    private var logger: Logger? = null
    operator fun getValue(thisRef: Any?, property: Any?) = logger ?: LoggerFactory.getLogger(thisRef?.javaClass)!!
}