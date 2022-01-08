# syntax=docker/dockerfile:1
FROM gradle:7.3.3-jdk17@sha256:823c43fdda6850c7c41035af3512997c7ebc3c7d9c6f67c637fe6cd2f32c1330 as build
COPY . /app
WORKDIR /app
RUN gradle clean build

FROM eclipse-temurin:17.0.1_12-jre@sha256:33b0f76c5fabcd2ce02865b1f218f3a74642dc3a84f864870d98152152d43ced as jre
RUN apt update
# install dumb-init for proper handling of the process in a container-context
RUN apt install dumb-init
# add a "javauser" as non-root user
RUN adduser javauser
# copy the distribution built by gradle via the jdk17 to the jre image
COPY --from=build /app/app/build/distributions/app.tar /opt
WORKDIR /opt
# extract the distribution
RUN tar -xvf app.tar
WORKDIR /opt/app
# Clean up APT when done.
RUN apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*
# make sure the javauser has the rights to run our process
RUN chown -R javauser /opt/app
# Switch to non-root user "javauser"
USER javauser
# Start it using the dumb-init process
CMD "dumb-init" "bin/app"