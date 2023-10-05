import org.springframework.boot.gradle.tasks.bundling.BootJar

val jar: Jar by tasks
val bootJar: BootJar by tasks

jar.enabled = true
bootJar.enabled = false

plugins {

}


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}
