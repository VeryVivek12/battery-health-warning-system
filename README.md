# Battery Health Warning System

## Prerequisite

- Java 21
- Docker

## How to run the project

### Infrastructure

Kafka is required for messaging functionality, docker compose file contains 1 zookepper, 2 kafka broker and kafka-ui
instance. start them using following command

```shell
docker compose up -d
```

services will be available on localhost as following ports

| Service   | Port  |
|-----------|-------|
| zookeeper | 22181 |
| kafka-1   | 29092 |
| kafka-2   | 39092 |
| kakfa-ui  | 8081  |

### stop the project

To stop the running infrastructure, run following command

```shell
docker compose down
```