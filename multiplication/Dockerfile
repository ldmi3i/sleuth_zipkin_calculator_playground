FROM openjdk:11
COPY build/libs/multiplication-*.jar multiplication.jar
EXPOSE 8019
CMD ["java", "-Dcom.sun.management.jmxremote", "-jar", "multiplication.jar"]