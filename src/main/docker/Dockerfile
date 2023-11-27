#设置运行所需的java环境
FROM openjdk:8-jre
#设置指向了容器内的/tmp的目录，由于 Spring Boot 使用内置的Tomcat容器，
#Tomcat 默认使用/tmp作为工作目录。这个命令的效果是：在宿主机的/var/lib/docker目录下
#创建一个临时文件并把它链接到容器中的/tmp目录。
#注意：VOLUME 命令只能指定挂载点，也就是说在此处，只能指定容器内的目录，不能指定宿主机上对应的目录，
#也就是说是宿主机上自动生成的目录 去挂载了  容器内的指定的/tmp目录！！！
#如果想要指定宿主机的目录去进行挂载，只能通过run命令中的 -v参数进行 宿主机的指定目录挂载容器内的指定目录
VOLUME /tmp
#设置作者名称
MAINTAINER EricFRQ
#将此路径下所有的jar包都添加到镜像中,并重命名为system.jar
ADD *.jar system.jar
#添加启动镜像时的命令，这里仅设置了启动默认端口
CMD ["--server.port=10012"]
#设置对外暴露的端口
EXPOSE 10012
#ENTRYPOINT 配置容器启动后执行的命令，并且不可被 docker run 提供的参数覆盖。
#-Djava.security.egd=file:/dev/./urandom
#添加java.security.egd的系统属性指向/dev/urandom，JVM上的随机数与熵池策略
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dport=10012","-jar","/system.jar"]