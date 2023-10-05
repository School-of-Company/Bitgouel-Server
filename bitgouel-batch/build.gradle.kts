plugins {

}

dependencies {
    implementation(project(":bitgouel-domain"))
    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("mysql:mysql-connector-java")
    testImplementation("org.springframework.batch:spring-batch-test")
}
