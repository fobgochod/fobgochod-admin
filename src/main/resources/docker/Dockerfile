FROM openjdk:17.0.2-jdk-oracle
WORKDIR /usr/local/fobgochod
COPY . ./
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime \
    && echo $TZ > /etc/timezone \
    && chmod +x /usr/local/fobgochod/startup.sh

ENTRYPOINT ["/usr/local/fobgochod/startup.sh"]
