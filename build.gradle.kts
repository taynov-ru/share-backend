plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.3.3"
	id("io.spring.dependency-management") version "1.1.6"
	kotlin("plugin.jpa") version "1.9.25"
	id("org.openapi.generator") version "7.8.0"
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
	mavenLocal()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("net.logstash.logback:logstash-logback-encoder:7.2")
	implementation("org.zalando:logbook-spring-boot-starter:3.7.2")
	implementation("org.zalando:logbook-servlet:3.7.2")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.security:spring-security-config")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.3.0")
	runtimeOnly ("org.postgresql:postgresql:42.7.2")
	implementation("org.liquibase:liquibase-core:4.26.0")
	implementation ("io.minio:minio:8.5.12")
	implementation ("org.apache.commons:commons-lang3:3.13.0")
	implementation ("org.hibernate.orm:hibernate-core:6.6.0.Final")
	implementation("org.hibernate.common:hibernate-commons-annotations:6.0.6.Final")
	implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
	compileOnly("io.swagger.core.v3:swagger-annotations:2.2.10")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	compileOnly ("org.openapitools:jackson-databind-nullable:0.2.1")
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
		"library" to "spring-boot",
	)

tasks.withType<Test> {
	useJUnitPlatform()
}

sourceSets {
	main {
		java {
			srcDir ("${buildDir.absolutePath}/generated/src/main/kotlin")
		}
	}
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("generateClient") {
	generatorName = "kotlin-spring"
	inputSpec.set("$projectDir/src/main/resources/openapi.yaml")
	outputDir.set("$buildDir/generated")
	apiPackage.set("ru.taynov.openapi.client")
	modelPackage.set("ru.taynov.openapi.model")
	configOptions.set(
		openApiConfigOptions + mapOf(
			"useJakartaEe" to "true",
			"useSpringBoot3" to "true",
		)
	)
	modelNameSuffix.set("Gen")
}

tasks.compileKotlin {
	dependsOn("generateClient")
}

tasks.jar {
	enabled = false
}
