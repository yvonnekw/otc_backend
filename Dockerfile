
# Base image
FROM openjdk:20

# Set working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/otc-springboot-backend-image.jar /app/otc-springboot-backend-image.jar

# Expose port
EXPOSE 8000

# Command to run the application
CMD ["java", "-jar", "otc-springboot-backend-image.jar"]





# Base image
#FROM openjdk:20

# Set working directory
#WORKDIR /app

# Copy the JAR file into the container
#COPY target/otc-springboot-backend-image.jar /app/otc-springboot-backend-image.jar

# Expose port
#EXPOSE 8000

# Command to run the application
#CMD ["java", "-jar", "otc-springboot-backend-image.jar"]

#VOLUME /tmp

#COPY /target/*.jar app.jar

# Expose port
#EXPOSE 8000
#ADD /target/otc-springboot-backend-image.jar otc-springboot-backend-image.jar
#ENTRYPOINT ["java","-jar","/otc-springboot-backend-image.jar"]