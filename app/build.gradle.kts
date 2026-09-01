plugins {
    application
    jacoco
    id("com.google.protobuf") version "0.9.6"
    `jvm-test-suite`
    id("com.diffplug.spotless") version "7.0.2"
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

val grpcVersion = "1.78.0"
val protoVersion = "4.36.1"

dependencies {
    // This dependency is used by the application.
    implementation("com.google.guava:guava:33.5.0-jre")

    // grpc (see https://github.com/grpc/grpc-java)
    implementation("io.grpc:grpc-netty-shaded:$grpcVersion")
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-stub:$grpcVersion")
    compileOnly("org.apache.tomcat:annotations-api:6.0.53") // necessary for Java 9+

    implementation("com.google.protobuf:protobuf-java:$protoVersion")
    implementation("com.google.protobuf:protobuf-java-util:$protoVersion")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:$protoVersion"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                create("grpc")
            }
        }
    }
}

sourceSets {
    main {
        java {
            srcDir("build/generated/source/proto/main/grpc")
            srcDir("build/generated/source/proto/main/java")
        }
    }
}

application {
    // Define the main class for the application.
    mainClass.set("tickserver.TickServerSimulator")
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter() // Use JUnit Jupiter for testing
        }
    }
}

spotless {
    java {
        googleJavaFormat()
        // protobuf/gRPC stubs are generated, not authored — don't format-gate them
        targetExclude("build/generated/**")
    }
}

jacoco {
    toolVersion = "0.8.14"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

// Keep generated protobuf/gRPC classes out of the JaCoCo report so stubs
// don't skew the coverage gate. Top-level afterEvaluate — see bootstrap docs.
afterEvaluate {
    tasks.jacocoTestReport {
        classDirectories.setFrom(classDirectories.files.map {
            fileTree(it) { exclude("**/build/generated/**") }
        })
    }
}
