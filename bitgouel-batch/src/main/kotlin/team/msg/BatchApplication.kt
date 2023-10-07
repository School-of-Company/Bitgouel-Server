package team.msg

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import kotlin.system.exitProcess

@SpringBootApplication
class BatchApplication

fun main(args: Array<String>) {
    val applicationContext = runApplication<BatchApplication>(*args)
    exitProcess(SpringApplication.exit(applicationContext)) // 배치 완료 후 바로 process 종료
}
