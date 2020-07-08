# inspiration-notebook 记录你的灵感

这是inspiration-notebook的后台工程文件，使用spring boot框架编写。<br />

## 开发须知
在开发之前请先更改`/src/main/resources/application-dev.yml`以及`/src/main/resources/application-prod.yml`这两个文件中的数据库配置。<br />
以及更改`/src/main/java/com.shadowingszy.tools/Tools.java`中的`zipPath`变量，该变量是指定生成的zip文件在服务器文件系统中的位置。<br />

## 部署须知
1、首先需要安装maven
2、执行`maven clean`清理项目
3、执行`maven install`生成jar文件
4、在`target`目录下即可找到生成的`inspiration-notebook-x.x.x.jar`
5、将`inspiration-notebook-x.x.x.jar`放入服务器中，执行`java -jar inspiration-notebook-x.x.x.jar`
6、访问服务器的8081端口即可