package team.msg

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["team.msg"])
@ConfigurationPropertiesScan
class BitgouelApplication

fun main(args: Array<String>) {
    runApplication<BitgouelApplication>(*args)
}

