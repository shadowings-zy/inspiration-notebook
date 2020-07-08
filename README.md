# inspiration-notebook 记录你的灵感

inspiration-notebook 是一款多人协作笔记本 web 应用<br />

## 前端项目

`inspiration-notebook-front-end`目录下是 inspiration-notebook 的前端工程文件，使用 vue 框架编写。<br />

### 开发须知

开发之前请更改`inspiration-notebook-front-end/config.json`文件中的 url 参数，`downloadZipUrl`是下载笔记的 url，`devServerUrl`是开发时使用的 devServer 的 url。

#### 安装依赖

```
npm install
```

#### 开发应用

```
npm run serve
```

#### 打包项目

```
npm run build
```

## 后台项目

`inspiration-notebook-back-end`目录下是 inspiration-notebook 的后台工程文件，使用 spring boot 框架编写，需要安装 maven。<br />

### 开发须知

在开发之前请先更改`inspiration-notebook-back-end/src/main/resources/application-dev.yml`以及`inspiration-notebook-back-end/src/main/resources/application-prod.yml`这两个文件中的数据库配置。<br />
以及更改`inspiration-notebook-back-end/src/main/java/com.shadowingszy.tools/Tools.java`中的`zipPath`变量，该变量是指定生成的 zip 文件在服务器文件系统中的位置。<br />

## 部署流程

1、进入前端工程目录，执行`npm run build`，并将打包出的`dist`目录中的全部文件复制到`src/main/resources/public`目录下。<br />
2、执行`maven clean`清理项目。<br />
3、执行`maven install`生成 jar 文件。<br />
4、在`target`目录下即可找到生成的`inspiration-notebook-x.x.x.jar`。<br />
5、将`inspiration-notebook-x.x.x.jar`放入服务器中，执行`java -jar inspiration-notebook-x.x.x.jar`。<br />
6、访问服务器的 8081 端口即可。<br />
