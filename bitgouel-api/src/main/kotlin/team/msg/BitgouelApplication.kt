package team.msg

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
class BitgouelApplication

fun main(args: Array<String>) {
    runApplication<BitgouelApplication>(*args)
}
