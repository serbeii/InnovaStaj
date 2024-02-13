FROM eclipse-temurin:17-jre-jammy
LABEL authors="cserimer"

RUN apt update && apt upgrade -y && \
	apt-cache search zookeeper && \
	echo "test" && \
    apt-get install -y zookeeper && \
    adduser -D -u 1000 -H -h ${HOME} zookeeper && \
    addgroup zookeeper zookeeper

EXPOSE 2181 2888 3888

CMD ["zkServer", "start-foreground"]