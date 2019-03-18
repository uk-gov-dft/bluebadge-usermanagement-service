FROM java:8-alpine
ARG JAR_NAME
COPY "build/libs/${JAR_NAME}" "/usr/src/app/app.jar"
EXPOSE 8180 8181 8100
RUN echo ${JAR_NAME}
CMD ["java","-jar","/usr/src/app/app.jar"]
