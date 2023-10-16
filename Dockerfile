FROM maven:3.9.2-eclipse-temurin-20

RUN mkdir /opt/app
COPY . /opt/app
RUN cd /opt/app && \
    mvn clean install
RUN chmod a+rwx /opt/app/runtime/stockfish/stockfish-ubuntu-x86-64-avx2
CMD ["java", "-jar", "--enable-preview", "-agentlib:jdwp=transport=dt_socket,address=*:8000,server=y,suspend=n", "/opt/app/target/lichess-bot-jar-with-dependencies.jar"]
