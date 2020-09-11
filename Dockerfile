FROM openjdk:8
ADD target/rp-cloud.jar rp-cloud.jar
EXPOSE 80
ENTRYPOINT ["java" ,"-jar", "rp-cloud.jar"]