FROM openjdk:11
COPY build/libs/addition-*.jar addition.jar
EXPOSE 8019
CMD ["java", "-Dcom.sun.management.jmxremote", "-jar", "addition.jar"]