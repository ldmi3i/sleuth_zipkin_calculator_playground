FROM openjdk:11
COPY build/libs/division-*.jar division.jar
EXPOSE 8019
CMD ["java", "-Dcom.sun.management.jmxremote", "-jar", "division.jar"]