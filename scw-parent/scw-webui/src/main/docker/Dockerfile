FROM java:8

MAINTAINER ujiuye

ADD scw-*.jar /app.jar

ENV PORT "-Dserver.port=10000"

ENV EUREKA "-Deureka.client.service-url.defaultZone=http://localhost:8761/eureka,http://localhost:8762/eureka"

EXPOSE 10000

ENTRYPOINT ["sh","-c","java -jar $PORT $EUREKA /app.jar"]