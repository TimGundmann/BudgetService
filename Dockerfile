FROM openjdk:10.0.2-13-jre-sid

COPY target/budget.jar /app/budget.jar

CMD ["java", "-jar", "/app/budget.jar"]
