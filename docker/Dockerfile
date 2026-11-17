# ---------- Stage 1: resolve deps + build (Maven + JDK 17) ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build

# Copy ONLY the POM first — this layer caches all Maven Central downloads
COPY pom.xml .
RUN mvn -B -e dependency:go-offline

# The published fat jar is the only input. The client targets Java 13, so the
# runtime below is Temurin 17 rather than the JDK 11 images used by older clients.
RUN mvn -B package -DskipTests \
    && mvn -B dependency:copy-dependencies -DoutputDirectory=target/lib

# ---------- Stage 2: runtime (JDK 17) ----------
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app
COPY --from=build /build/target/lib /app/lib

# Connection string for configuration server. Override at runtime with -e LLCONFIG=...
# LOGGER_CONFIG and CONFIG_FILE_URI are derived from this in entrypoint.sh.
ENV LLCONFIG=http://lablink-config:10101/get?id=

# OPC UA client configuration id (appended to LLCONFIG).
ENV OPCUA_CONFIG=lablink-opcuaclient.config

# Path to class implementing the main routine.
ENV OPCUA_CLASS=at.ac.ait.lablink.clients.opcuaclient.BasicOpcUaClient

# Path to Java JAR file of the OPC UA client.
ENV OPCUA_JAR_FILE=/app/lib/opcuaclient-0.0.3-jar-with-dependencies.jar

# Specify the maximum size (in bytes) of the memory allocation pool.
ENV MEMORY_FLAG=-Xmx512m

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]
