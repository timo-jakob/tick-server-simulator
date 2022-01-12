# syntax=docker/dockerfile:1
FROM gradle:7.3.3-jdk17@sha256:b0e49b4642c9bd069f4e6f74482f54159b6d76b48d73506801790440f07e3e20 as build
COPY . /app
WORKDIR /app
RUN gradle clean build

FROM eclipse-temurin:17.0.1_12-jre@sha256:9ef0e35ee536bbe99496c11da2f462e89805923c04ccd57671685ae1add5e71d as jre
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