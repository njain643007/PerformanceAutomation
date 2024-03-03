FROM ubuntu:20.04
RUN apt-get -y update
RUN apt-get -y install openjdk-11-jdk
RUN apt-get -y install wget
RUN apt-get -y install nano
RUN wget https://dlcdn.apache.org//jmeter/binaries/apache-jmeter-5.6.2.tgz
RUN tar -xf apache-jmeter-5.6.2.tgz
RUN wget https://jmeter-plugins.org/get -O jmeter-plugins-manager.jar
WORKDIR apache-jmeter-5.6.2/lib
RUN wget https://repo1.maven.org/maven2/kg/apc/cmdrunner/2.2.1/cmdrunner-2.2.1.jar
WORKDIR ext/
RUN wget https://repo1.maven.org/maven2/kg/apc/jmeter-plugins-manager/1.6/jmeter-plugins-manager-1.6.jar
WORKDIR apache-jmeter-5.6.2/lib
RUN java  -jar /apache-jmeter-5.6.2/lib/cmdrunner-2.2.1.jar --tool org.jmeterplugins.repository.PluginManagerCMD install jpgc-casutg
WORKDIR /
COPY . /jmeter-automation/
WORKDIR /
ARG JMX_FILE=value
ARG TARGET_CONCURRENCY=value
ARG RAMPUP_TIME=value
ARG RAMPUP_STEPS=value
ENV JMX_FILE1 = $JMX_FILE
ENV TO_EMAIL=value
ENV CC_EMAIL=value
ENV MAIL_SUBJECT=value
ENV JOB_NAME=value
ENV BUILD_NUMBER=value
ENV DEV_USER=""
ENV ENVIRONMENT=value
#CMD ["./apache-jmeter-5.6.2/bin/jmeter","-n", "-t", "./jmeter-automation/jmxfiles/Login.jmx", "-l", "./jmeter-automation/results.jtl", "-e", "-o", "./jmeter-automation/TestReport/Dashboard"]
RUN /apache-jmeter-5.6.2/bin/jmeter -n -t /jmeter-automation/jmxfiles/${JMX_FILE} -Jtarget_concurrency=${TARGET_CONCURRENCY} -Jrampup_steps=${RAMPUP_STEPS} -Jrampup_time=${RAMPUP_TIME} -l /jmeter-automation/results.jtl -e -o /jmeter-automation/TestReport/Dashboard
WORKDIR jmeter-automation
CMD ["sh", "-c", "java -jar PerformanceAutoamtion-1.0.0-jar-with-dependencies.jar \"${TO_EMAIL}\" \"${CC_EMAIL}\" \"${JMX_FILE1}\""]

