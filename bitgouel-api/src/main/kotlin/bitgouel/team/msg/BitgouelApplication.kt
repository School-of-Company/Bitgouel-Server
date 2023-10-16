package bitgouel.team.msg

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication(scanBasePackages = ["team.msg.domain"])
class BitgouelApplication

fun main(args: Array<String>) {
    runApplication<BitgouelApplication>(*args)
}

