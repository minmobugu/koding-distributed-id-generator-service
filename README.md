# koding-distributed-id-generator-service

`koding-distributed-id-generator-service` is a standalone distributed ID generation service. It provides two ID allocation modes:

- Segment mode backed by MySQL
- Snowflake mode coordinated by ZooKeeper

The repository uses a Maven multi-module layout with separate API and business modules.

## Modules

- `koding-distributed-id-generator-api`
  - OpenFeign contract for downstream callers
  - Defines the service name and ID generation endpoints
- `koding-distributed-id-generator-biz`
  - Spring Boot application
  - HTTP controllers
  - Segment and Snowflake generator implementations
  - MyBatis DAO layer and local runtime configuration

## Tech Stack

- Java 21
- Spring Boot 3.2.4
- Spring Cloud 2023.0.1
- Spring Cloud Alibaba 2023.0.1.0
- MyBatis
- Druid
- MySQL
- ZooKeeper / Curator

## Main Endpoints

- `GET /id/segment/get/{key}`: generate an ID with segment mode
- `GET /id/snowflake/get/{key}`: generate an ID with snowflake mode
- `GET /cache`: inspect in-memory segment cache
- `GET /db`: inspect Leaf allocation records
- `GET /decodeSnowflakeId?snowflakeId=...`: decode a snowflake ID

## Configuration

Key files:

- `koding-distributed-id-generator-biz/src/main/resources/config/application.yml`
  - local HTTP port, currently `8003`
- `koding-distributed-id-generator-biz/src/main/resources/config/bootstrap.yml`
  - Nacos discovery settings
- `koding-distributed-id-generator-biz/src/main/resources/leaf.properties`
  - Segment MySQL settings
  - Snowflake ZooKeeper settings

Current implementation facts:

- Segment mode uses the JDBC settings in `leaf.properties`
- Snowflake mode uses the ZooKeeper address and local worker port in `leaf.properties`
- Nacos discovery is configured in `bootstrap.yml`

## Build

```bash
mvn clean compile
mvn test
mvn clean package
```

Build only the business module:

```bash
mvn -pl koding-distributed-id-generator-biz -am compile
```

## Run

```bash
mvn -pl koding-distributed-id-generator-biz -am spring-boot:run
```

Or package first and run the jar from `koding-distributed-id-generator-biz/target/`.

## Notes

- The API module and controller paths should stay in sync.
- The monitor controller returns view names `segment` and `db`, but the repository currently does not include matching template files.
- The repository currently does not contain committed automated tests.
