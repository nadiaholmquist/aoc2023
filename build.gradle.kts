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

	testImplementation(kotlin("test"))
}

kotlin {
	jvmToolchain(19)
}

application {
	mainClass.set("MainKt")
}

tasks.test {
	useJUnitPlatform()
}

tasks.jar {
	manifest.attributes["Main-Class"] = "MainKt"
	val dependencies = configurations.runtimeClasspath.get().map(::zipTree)
	from(dependencies)
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}