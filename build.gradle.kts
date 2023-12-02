plugins {
	kotlin("jvm") version "1.9.20"
	application
}

group = "sh.nhp"
version = "1.0-SNAPSHOT"

repositories {
	mavenCentral()
}

kotlin {
	jvmToolchain(19)
}

application {
	mainClass.set("MainKt")
}