package spartacodingclub.nbcamp.kotlinspring.assignment.section.motpsimulator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class MotpsimulatorApplication

fun main(args: Array<String>) {
    runApplication<MotpsimulatorApplication>(*args)
}
