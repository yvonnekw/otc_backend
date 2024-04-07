
FROM openjdk:21

WORKDIR /app

COPY target/otc-springboot-backend-image.jar /app/otc-springboot-backend-image.jar

EXPOSE 8000


CMD ["java", "-jar", "otc-springboot-backend-image.jar"]


