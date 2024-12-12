FROM eclipse-temurin:21.0.5_11-jre@sha256:8e201486c2c51c07f0c1e33bf3841a03b2d624cd3dd8a734a1ece02ac48b795a as jre
RUN apt update
# install dumb-init for proper handling of the process in a container-context
RUN apt install dumb-init
# add a "javauser" as non-root user
RUN adduser javauser
# copy the distribution built by gradle via the jdk17 to the jre image
COPY app/build/distributions/app*.tar /opt
WORKDIR /opt
# extract the distribution
RUN tar -xvf app*.tar
RUN mkdir /opt/app
RUN mv /opt/app*/* /opt/app/
WORKDIR /opt/app
# Clean up APT when done.
RUN apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*
# make sure the javauser has the rights to run our process
RUN chown -R javauser /opt/app
# Switch to non-root user "javauser"
USER javauser
# Start it using the dumb-init process
CMD "dumb-init" "bin/app"