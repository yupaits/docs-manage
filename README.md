# DocsManage
文档管理系统


### 版本说明

- v1.1.0
    
    - docs-commons: 后台通用代码
    - docs-service: 文档管理后台服务，使用无状态的jwt做认证授权
    - docs-web: 文档管理前台web界面，采用vue-cli构建，基于Vue生态开发的SPA（单页面应用）
    - sso: 统一鉴权中心，用于token的生成和刷新等


- v1.0.0
    
    合并后台服务和前台web，后台使用 shiro + jwt 完成认证授权功能。

- v0.0.1-SNAPSHOT

    - docs-service: 文档管理后台服务，使用 spring security + jwt 做认证授权
    - docs-web: 文档管理前台web界面（大部分页面编写完成，部分功能以完成）

### 启动部署

- v1.1.0

    - docs-commons
         `mvn clean package` 打包生成 jar 文件，为 docs-service 和 sso 提供一些基础封装类
        
    - docs-service 和 sso
        使用 `mvn clean package` 打包生成 jar 文件，执行 `java -jar target/*.jar` 命令运行后台服务。
    
    - docs-web  
        依次使用 `npm install` 和 `npm run build` 生成 index.html 及 css、js 等文件，将生成的静态文件部署至 Nginx。  
        修改 Nginx 配置文件 nginx.conf:  
        ```
        server {
            listen       3000;
            server_name  localhost;
            root         [pathToProject]/docs-manage/docs-web/dist/;
            index        index.html;
        }
        ```  
        启动 Nginx，浏览器打开 http://localhost:3000/ 。
- v1.0.0
    
    使用 `mvn clean` + `mvn package` 命令生成 **war** 包，将 **war** 包部署至 **tomcat** 等 web 容器中。在浏览器访问 https://localhost:9000/。

- v0.0.1-SNAPSHOT

    - docs-service  
        使用 `mvn clean package` 打包生成 jar 文件，执行 `java -jar target/*.jar` 命令运行文档管理后台服务。
    
    - docs-web  
        修改 Nginx 配置文件 nginx.conf:  
        ```
        server {
            listen       3000;
            server_name  localhost;
            root         [pathToProject]/docs-manage/docs-web/;
            index        index.html;
        }
        ```  
        启动 Nginx，浏览器打开 http://localhost:3000/ 。
