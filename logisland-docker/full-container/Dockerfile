FROM centos:7
MAINTAINER Groupe SEB

USER root

ARG PROXY

ENV http_proxy $PROXY
ENV https_proxy $PROXY
ENV ftp_proxy $PROXY

# install dev tools
RUN yum clean all; \
    rpm --rebuilddb; \
    yum install -y curl which tar sudo wget vim;

# Java 8
RUN wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/8u131-b11/d54c1d3a095b4ff2b6607d096fa80163/jre-8u131-linux-x64.rpm" && yum -y localinstall jre-8u131-linux-x64.rpm && rm jre-8u131-linux-x64.rpm
ENV JAVA_HOME /usr

# Spark
RUN curl -s http://d3kbcqa49mib13.cloudfront.net/spark-2.1.0-bin-hadoop2.7.tgz | tar --owner=root --group=root -xz -C /usr/local/
RUN cd /usr/local && ln -s spark-2.1.0-bin-hadoop2.7 spark
ENV SPARK_HOME /usr/local/spark
ENV PATH $PATH:$SPARK_HOME/bin
EXPOSE 4040

# Logisland
COPY logisland-*.tar.gz /usr/local/
RUN cd /usr/local; \
    tar -xzf logisland-*.tar.gz; \
    ln -s /usr/local/logisland-0.13.0 /usr/local/logisland; \
    mkdir /usr/local/logisland/log; \
    rm -f /usr/local/*.gz
ENV LOGISLAND_HOME /usr/local/logisland
RUN mv /usr/local/logisland/conf/log4j.properties /usr/local/spark/conf
ENV PATH $PATH:$LOGISLAND_HOME/bin
WORKDIR $LOGISLAND_HOME/

# Externalize LogIsland conf directory
VOLUME ["/usr/local/logisland/conf"]

# update boot script
COPY bootstrap.sh /etc/bootstrap.sh
RUN chown root:root /etc/bootstrap.sh
RUN chmod 700 /etc/bootstrap.sh

ENTRYPOINT ["/etc/bootstrap.sh"]
