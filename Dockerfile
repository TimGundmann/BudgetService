FROM openjdk:8-jre-alpine

COPY target/budget.jar /app/budget.jar

CMD ["java", "-jar", "/app/budget.jar"]
