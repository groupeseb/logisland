LogIsland docker files
======================

Logisland distribution for development and testing purpose :

- Logisland 0.12.2

This repository contains a Docker file to build a Docker image with Apache Spark, HBase, Flume & Zeppelin. 
This Docker image depends on [centos 7](https://github.com/CentOS/CentOS-Dockerfiles) image.

Build your own
--------------

Building the image

.. code-block:: sh

    mvn clean install -Dhdp=2.5 -Pdocker -DskipTests

If proxy needed to build the image (yum, wget ...) :

.. code-block:: sh

    mvn clean install -Dhdp=2.5 -Pdocker -DskipTests -Dproxy=http://proxyuser:proxypass@proxyhost:proxyport


Running the image
-----------------

.. code-block::

    docker run -d groupeseb/logisland-hdp2.5

if you want to mount a directory from your host, add the following option :

.. code-block::

    -v ~/projects/logisland/docker/mount/:/usr/local/logisland
