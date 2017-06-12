# LogIsland UI Title

This is the web front-end of logisland to manage jobs and configurations

## Getting Started

For test and development purpose, the GUI can be started using the nodejs live-server
```
$ npm install jspm -g, or sudo npm install jspm
$ jspm install, or sudo jspm install
$ live-server --open=app
```

### Prerequisites

Install nodejs

then install jspm and live-server
```
$ npm install jspm live-server -g
```

## How to debug the ui
Perform the following steps:

1) start Kafka and Zookeeper
sudo docker run -p 2181:2181 -p 9092:9092 --env ADVERTISED_HOST=localhost --env ADVERTISED_PORT=9092 spotify/kafka

2) start the agent in debug mode as specified in the document of the project logisland-agent


3) run the ui
live-server --open=app
