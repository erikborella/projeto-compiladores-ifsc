FROM maven:3.9.4-eclipse-temurin-21-alpine AS server_build

WORKDIR /api

COPY projeto-compiladores-ifsc/pom.xml ./
RUN mvn dependency:go-offline

COPY projeto-compiladores-ifsc/src ./src

RUN mvn clean package -DskipTests --batch-mode

# -----

FROM node:20.17.0-alpine AS client_build

WORKDIR /client

COPY client/package*.json ./
RUN npm install

ARG BASE_URL=http://localhost
ARG BASE_URL_WEB_SOCKET_ADDRESS=${BASE_URL}/compiler/runner/ws

ENV VITE_BASE_URL=${BASE_URL}
ENV VITE_WEB_SOCKET_ADDRESS=${BASE_URL_WEB_SOCKET_ADDRESS}

COPY client .
RUN npm run build

# -----

FROM eclipse-temurin:21-jre-jammy AS dependencies

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

RUN apt-get clean
RUN rm -rf /var/lib/apt/lists/*

FROM dependencies AS final

ARG BASE_URL=http://localhost
ARG API_APP_NAME="COMPILADORES IFSC"

ENV spring.application.name=${API_APP_NAME}
ENV cors.allowed-origins=${BASE_URL}

WORKDIR /app

COPY --from=server_build /api/target ./
RUN mv *.jar api.jar

COPY --from=client_build /client/dist /var/www/html

COPY docker/nginx.conf /etc/nginx/nginx.conf
COPY docker/start-services.sh ./start-services.sh

RUN chmod u+x ./start-services.sh

EXPOSE 80 8080

CMD ["./start-services.sh"]