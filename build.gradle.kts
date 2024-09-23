plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.3.3"
	id("io.spring.dependency-management") version "1.1.6"
	kotlin("plugin.jpa") version "1.9.25"
	id("org.openapi.generator") version "5.2.0"
}

group = "ru.taynov"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

apply(plugin = "org.openapi.generator")

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	compileOnly ("org.openapitools:jackson-databind-nullable:0.2.1")
	compileOnly("io.swagger:swagger-annotations:1.6.3")
	compileOnly("io.springfox:springfox-core:3.0.0")
	compileOnly ("javax.validation:validation-api:2.0.1.Final")
	compileOnly("javax.annotation:javax.annotation-api:1.3.2")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

	val openApiConfigOptions = mapOf(
		"dateLibrary" to "java8-localdatetime",
		"skipDefaultInterface" to "true",
		"serializableModel" to "true",
		"interfaceOnly" to "true",
		"hideGenerationTimestamp" to "true",
		"useTags" to "true",
		"library" to "spring-cloud"
	)

tasks.withType<Test> {
	useJUnitPlatform()
}

sourceSets {
	main {
		java {
			srcDir ("${buildDir.absolutePath}/generated/src/main/java")
		}
	}
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("generateClient") {
	generatorName = "spring"
	inputSpec.set("$projectDir/src/main/resources/swagger.yaml")
	outputDir.set("$buildDir/generated")
	apiPackage.set("ru.taynov.swagger.client")
	modelPackage.set("ru.taynov.swagger.model")
	configOptions.set(openApiConfigOptions)
	modelNameSuffix.set("Gen")
}

tasks.compileKotlin {
	dependsOn("generateClient")
}