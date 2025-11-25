FROM maven:3.9.11-eclipse-temurin-25

WORKDIR /opt/app
COPY pom.xml ./
COPY ./runtime/stockfish_linux ./runtime/stockfish_linux
RUN mvn dependency:go-offline

COPY ./src ./src
RUN mvn clean install

RUN chmod a+rwx ./runtime/stockfish_linux/stockfish-ubuntu-x86-64-avx2

CMD ["java", "-jar", "--enable-preview", "-agentlib:jdwp=transport=dt_socket,address=*:8000,server=y,suspend=n", "target/lichess-bot-jar-with-dependencies.jar"]
