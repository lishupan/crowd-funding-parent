#指定基础镜像
FROM java:8

#拷贝jar包到镜像
ADD scw-register-1.0.jar /app.jar

#设置环境参数1 传递服务器启动端口
ENV PORT  "-Dserver.port=8761"

#设置环境参数2 指定注册地址
ENV EUREKA "-Deureka.client.service-url.defaultZone=http://localhost:8761/eureka"

#声明默认暴露端口
EXPOSE 8761

#当容器启动的时候，我们要运行程序
ENTRYPOINT ["sh","-c","java -jar $PORT $EUREKA /app.jar"]