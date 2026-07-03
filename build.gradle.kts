plugins {
    id("com.netflix.nebula.release") version "21.0.0"
}

tasks.register<Exec>("buildDocker") {
    // build a docker file with an appropriate version
    commandLine("docker")
    args("build", "-t", "${rootProject.name}:${version}", ".")
}
