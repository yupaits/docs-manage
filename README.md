# DocsManage 之 Shior-jwt 实现认证授权

## 启动步骤

1. 下载代码

    ```bash
    git clone https://github.com/YupaiTS/docs-manage.git
    ```

1. 切换到 `shiro-jwt` 分支

1. 创建数据库

    ```sql
    create database  `docs` default charset utf8 collate utf8_general_ci;
    ```

1. 执行 `test` 包下的测试用例，向数据库插入测试数据

1. 执行 `docs-server` 下 `DocsApplication.java` 类的 `main` 方法，运行服务端程序

1. 使用命令行进入 `docs-ui` 目录，分别执行 `npm install` 和 `npm run dev` 命令安装依赖包和运行前端程序

1. 在浏览器中打开 [http://localhost:8080](http://localhost:8080) 进入文档管理系统

