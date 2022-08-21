FROM java:8
ENV MYPATH /usr/local
WORKDIR $MYPATH
COPY ./handleData-core/target/*.jar /usr/local/application.jar
CMD chmod 777 /usr/local/*.jar
ENTRYPOINT ["java","-jar","/usr/local/application.jar"]