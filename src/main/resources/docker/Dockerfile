FROM openjdk:8-jdk-alpine
WORKDIR /usr/local/fobgochod
COPY . ./
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime \
    && echo $TZ > /etc/timezone \
    && chmod +x /usr/local/fobgochod/startup.sh

ENTRYPOINT ["/usr/local/fobgochod/startup.sh"]
