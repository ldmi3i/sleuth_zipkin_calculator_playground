FROM openjdk:11
COPY build/libs/subtraction-*.jar subtraction.jar
EXPOSE 8019
CMD ["java", "-Dcom.sun.management.jmxremote", "-jar", "subtraction.jar"]