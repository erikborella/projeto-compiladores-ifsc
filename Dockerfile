FROM maven:3.9.4-eclipse-temurin-21 AS server_build

WORKDIR /api

COPY projeto-compiladores-ifsc/pom.xml ./
RUN mvn dependency:go-offline

COPY projeto-compiladores-ifsc/src ./src

RUN mvn clean package -DskipTests

# -----

FROM node:20.17.0 as client_build

WORKDIR /client

COPY client .
RUN npm install

RUN npm run build

# -----

FROM eclipse-temurin:21-jdk-jammy as final

WORKDIR /app

COPY --from=server_build /api/target ./
RUN mv *.jar api.jar

# Atualiza os pacotes e instala dependências necessárias para adicionar novos repositórios
RUN apt-get update && apt-get install -y \
    lsb-release \
    wget \
    software-properties-common \
    gnupg \
    nginx

RUN wget https://apt.llvm.org/llvm.sh
RUN chmod +x llvm.sh
RUN ./llvm.sh 16

RUN rm ./llvm.sh
RUN rm -rf /var/lib/apt/lists/*

COPY --from=client_build /client/dist /var/www/html

COPY docker/nginx.conf /etc/nginx/nginx.conf
COPY docker/start-services.sh ./start-services.sh

RUN chmod u+x ./start-services.sh

EXPOSE 80 8080

CMD ./start-services.sh