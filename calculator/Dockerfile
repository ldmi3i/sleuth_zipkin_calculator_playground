FROM openjdk:11
COPY build/libs/calculator-*.jar calculator.jar
EXPOSE 8019
CMD ["java", "-Dcom.sun.management.jmxremote", "-jar", "calculator.jar"]