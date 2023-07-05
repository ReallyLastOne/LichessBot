FROM maven:3.9.2-eclipse-temurin-20
RUN mkdir /opt/app
COPY . /opt/app
RUN cd /opt/app && \
    mvn clean install
CMD ["java", "-jar", "/opt/app/target/lichess-bot-jar-with-dependencies.jar"]
