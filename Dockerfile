FROM openjdk:8-jre
MAINTAINER nyx <dnyx@riseup.net>

RUN useradd \
        --user-group \
        --no-create-home \
        --uid 1000 \
        --shell /bin/false \
        app

ARG JAR_FILE
# Add the service itself
ADD target/$JAR_FILE /usr/share/xandria/xandria.jar

# Drop privs
USER app

ENTRYPOINT ["java", "-jar", "/usr/share/xandria/xandria.jar"]
