# syntax=docker/dockerfile:1

# ---------- Estágio 1: build + testes unitários ----------
# Imagem com JDK + Maven: pesada, mas só existe durante o build.
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Dependências primeiro: enquanto o pom.xml não mudar, esta camada vem do cache.
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -B -q dependency:go-offline

COPY src ./src
# "package" compila e roda os testes unitários (surefire). Teste vermelho = build quebra = não há imagem.
# Os testes de integração/aceitação (*IT) precisam de Docker para o Postgres e rodam na esteira (mvn verify).
RUN --mount=type=cache,target=/root/.m2 mvn -B package

# ---------- Estágio 2: runtime ----------
# Só o JRE (sem compilador, sem Maven, sem código-fonte): imagem menor e com menos superfície de ataque.
FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S app && adduser -S app -G app
WORKDIR /app
COPY --from=build /app/target/hemocentro.jar app.jar

# Não roda como root
USER app

ARG APP_VERSION=local
ENV APP_VERSION=${APP_VERSION} \
    JAVA_OPTS="-XX:MaxRAMPercentage=75"

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
