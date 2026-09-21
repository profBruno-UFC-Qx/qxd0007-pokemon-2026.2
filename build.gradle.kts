plugins {
    id("java")
}

group = "br.ufc.qx"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.0")
}

tasks.test {
    useJUnitPlatform()
}