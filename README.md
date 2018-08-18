# DocsManage 之 实现基于OAuth2的认证授权和单点登录

## 分支

- [shiro-jwt](https://github.com/YupaiTS/docs-manage/tree/shiro-jwt): DocsManage 之 Shiro-jwt 实现认证授权
- [spring-security-jwt](https://github.com/YupaiTS/docs-manage/tree/spring-security-jwt): DocsManage 之 spring-security-jwt 实现认证授权

## 启动步骤

1. 下载代码

    ```bash
    git clone https://github.com/YupaiTS/docs-manage.git
    ```

1. 切换到 `master` 分支

1. 创建数据库

    ```sql
    create database  `docs-oauth2` default charset utf8 collate utf8_general_ci;
    ```

1. 执行 `auth-server` 和 `docs-server` 的 `test` 包下的测试用例，向数据库插入测试数据

1. 依次运行 `discovery-server`、`auth-server`、`docs-server`、`docs-client` 中 `Application` 类的 `main` 方法启动服务

1. 在浏览器中 `http://localhost:11030/` 进入客户端页面