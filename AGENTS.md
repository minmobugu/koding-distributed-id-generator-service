# AGENTS.md

## 目的

本文件用于说明在 `koding-distributed-id-generator-service` 仓库中工作的 Agent 或开发者，应如何理解项目结构、定位代码和验证修改。

当前仓库是独立的分布式 ID 生成服务，技术栈为 `Java 21 + Spring Boot 3.2 + Spring Cloud Alibaba + MyBatis + ZooKeeper`，采用 Maven 多模块结构。

## 工作区约束

本项目位于小番茄社区总工作区内，除本文件外，还必须遵守工作区根目录 `../AGENTS.md` 中的统一要求。

- 进入本项目协作前，还应先阅读工作区根目录 `../AGENTS.md`
- 未明确需要跨项目修改时，应优先只在 `koding-distributed-id-generator-service` 目录内操作
- 在任何仓库提交代码时，Git commit message 必须统一采用 Conventional Commits 规范，并使用全英文编写
- 统一格式：`<type>(<scope>): <subject>`
- 如需补充本服务的接口文档或设计文档，应遵循工作区根目录文档约束，统一维护在 `../docs` 体系内

## 构建与验证

- 使用系统 Maven；仓库内没有 `mvnw`
- 全量编译：`mvn clean compile`
- 全量测试：`mvn test`
- 全量打包：`mvn clean package`
- 安装到本地仓库：`mvn clean install`
- 仅构建 API 模块：`mvn -pl koding-distributed-id-generator-api -am compile`
- 仅构建业务模块：`mvn -pl koding-distributed-id-generator-biz -am compile`

## 仓库事实

### 模块结构

- `koding-distributed-id-generator-api`：Feign API 模块，对外暴露 ID 获取接口定义
- `koding-distributed-id-generator-biz`：业务启动模块，包含 HTTP Controller、Segment 与 Snowflake 两套发号实现

### 关键入口

- `pom.xml`：聚合工程，定义 Java 21、Spring Boot 3.2.4、Nacos、MyBatis、ZooKeeper 等依赖版本
- `koding-distributed-id-generator-biz/src/main/java/com/koding/distributed/id/generator/biz/KodingDistributedIdGeneratorBizApplication.java`：Spring Boot 启动入口
- `koding-distributed-id-generator-biz/src/main/resources/config/application.yml`：本地端口配置，当前端口为 `8003`
- `koding-distributed-id-generator-biz/src/main/resources/config/bootstrap.yml`：Nacos 注册中心配置
- `koding-distributed-id-generator-biz/src/main/resources/leaf.properties`：Segment 与 Snowflake 的本地核心配置

### 当前接口事实

- `GET /id/segment/get/{key}`：获取 Segment 模式 ID
- `GET /id/snowflake/get/{key}`：获取 Snowflake 模式 ID
- `GET /cache`：查看 Segment 缓存信息
- `GET /db`：查看 Leaf 分配表信息
- `GET /decodeSnowflakeId?snowflakeId=...`：解析 Snowflake ID

### 当前实现事实

- Segment 模式依赖 `leaf.properties` 中的 MySQL 连接配置
- Snowflake 模式依赖 `leaf.properties` 中的 ZooKeeper 地址和工作节点端口配置
- `koding-distributed-id-generator-api` 的 Feign 接口与 `biz` 模块控制器路径保持一致
- 监控控制器返回 `segment`、`db` 视图名，但当前源码中未看到对应模板文件
- 当前仓库未看到已提交的测试用例

## 修改原则

- 修改外部接口时，必须同步检查 `api` 模块和 `biz/controller` 中的路径与参数是否一致
- 修改 Segment 发号逻辑时，优先从 `core/segment`、`dao`、`service/SegmentService` 链路理解行为，不要只改 Controller
- 修改 Snowflake 发号逻辑时，必须同时关注时钟回拨、workerId 分配和 ZooKeeper 节点持有逻辑
- 修改配置项时，必须区分 `bootstrap.yml`、`application.yml` 和 `leaf.properties` 的职责边界
- 如果新增监控页面或模板，先确认现有 `LeafMonitorController` 的返回方式是否仍然成立

## 不要做的事

- 不要把本项目误判为普通单模块服务；它是 `api + biz` 双模块结构
- 不要只改 Feign 接口而不改控制器，或只改控制器而不改 Feign 接口
- 不要在不了解 `leaf.properties` 依赖含义的情况下随意重命名配置键
- 不要假设仓库里已经有完善的自动化测试覆盖；当前几乎没有测试保护

## 改动后至少检查什么

- 接口变更：检查 Feign API、Controller 路径、返回值是否一致
- 配置变更：检查 `application.yml`、`bootstrap.yml`、`leaf.properties` 是否仍然配套
- 发号逻辑变更：检查 Segment 与 Snowflake 两条路径是否仍可启动
- 打包变更：至少运行一次 `mvn -pl koding-distributed-id-generator-biz -am compile`
