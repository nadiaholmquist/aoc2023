plugins {
	kotlin("jvm") version "1.9.20"
	application
}

group = "sh.nhp"
version = "1.0-SNAPSHOT"

repositories {
	mavenCentral()
}

dependencies {
	implementation(kotlin("reflect"))
	implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
}

kotlin {
	jvmToolchain(19)
}

application {
	mainClass.set("MainKt")
}