FROM openjdk:11-oraclelinux7
RUN groupadd -r spring && useradd -r spring -g spring
USER spring:spring
EXPOSE 9682
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} GustavoRocha_FoodCalorieMeter-1.1.jar
ENTRYPOINT ["java","-jar","/GustavoRocha_FoodCalorieMeter-1.1.jar"]