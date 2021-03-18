FROM openjdk:11
COPY build/libs/exponentiation-*.jar exponentiation.jar
EXPOSE 8019
CMD ["java", "-Dcom.sun.management.jmxremote", "-jar", "exponentiation.jar"]