FROM eclipse-temurin:8-jdk

ENV WILDFLY_VERSION=10.1.0.Final
ENV WILDFLY_HOME=/opt/wildfly

# Baixa e instala o WildFly 10
RUN apt-get update && apt-get install -y curl unzip && \
    curl -L -o /tmp/wildfly.zip https://download.jboss.org/wildfly/10.1.0.Final/wildfly-10.1.0.Final.zip && \
    unzip /tmp/wildfly.zip -d /opt && \
    mv /opt/wildfly-10.1.0.Final ${WILDFLY_HOME} && \
    rm -rf /tmp/wildfly.zip && \
    apt-get remove -y unzip curl && \
    apt-get autoremove -y && \
    apt-get clean

# Copia o WAR gerado pelo Maven
COPY target/desafiotopaz.war ${WILDFLY_HOME}/standalone/deployments/

# Expõe a porta web
EXPOSE 8080

# Sobe o WildFly aceitando acesso externo
CMD ["/opt/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]