FROM openjdk:11-oraclelinux7
USER spring:spring
EXPOSE 9682
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} GustavoRocha_FoodCalorieMeter-1.0.jar
ENTRYPOINT ["java","-jar","/GustavoRocha_FoodCalorieMeter-1.0.jar"]