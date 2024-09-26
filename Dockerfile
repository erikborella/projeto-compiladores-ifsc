FROM maven:3.9.4-eclipse-temurin-21 AS server_build

WORKDIR /api

COPY projeto-compiladores-ifsc/pom.xml ./
RUN mvn dependency:go-offline

COPY projeto-compiladores-ifsc/src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY --from=server_build /api/target ./
RUN mv *.jar api.jar

# Atualiza os pacotes e instala dependências necessárias para adicionar novos repositórios
RUN apt-get update && apt-get install -y \
    lsb-release \
    wget \
    software-properties-common \
    gnupg

RUN wget https://apt.llvm.org/llvm.sh
RUN chmod +x llvm.sh
RUN ./llvm.sh 16

RUN rm ./llvm.sh
RUN rm -rf /var/lib/apt/lists/*

EXPOSE 8080

CMD [ "java", "-jar", "api.jar", "api" ]