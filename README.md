Crud Intellij Plugin
===============
一个增删改查的插件，可以根据数据库表结构，帮助您快速生成相关代码。

使用的方式有两种: 
- 从Crud Plugin 生成项目
- 已有项目右键选择生成代码

### 注意: 
1. 目前暂不支持字段类型的映射关系设置
2. 目前暂不支持表之间的对应关系处理
3. 为表和字段设置合适的注释。
4. 建议根据《阿里巴巴Java开发手册》的MySQL数据库规约来设计数据库表

### 支持的环境
环境 | 版本
---|---
Java | 1.8以上
Intellij Idea | 2017.3以上

## 插件安装
#### 旧版本idea：
  - <kbd>File</kbd> > <kbd>Settings</kbd> > <kbd>Plugins</kbd> > <kbd>Browse repositories...</kbd> > <kbd>Search for "crud"</kbd> > <kbd>Install Plugin</kbd>

![image](https://raw.githubusercontent.com/mars05/static/master/image/crud1.jpg)
#### 新版本idea：
  - <kbd>File</kbd> > <kbd>Settings</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "crud"</kbd> > <kbd>Install Plugin</kbd>  
  
![image](https://raw.githubusercontent.com/mars05/static/master/image/crud2.jpg)
#### 本地安装：
下载插件文件 [crud-plugin](https://github.com/mars05/crud-intellij-plugin/releases/download/v1.0.3/crud-plugin-1.0.3.zip)
## 使用教程

### 1.创建一个新的项目:
  - <kbd>New</kbd> > <kbd>Project</kbd> / <kbd>Module</kbd> > <kbd>Crud</kbd> > <kbd>数据库表选择</kbd>
  - 启动Application
  - 可使用Swagger测试请求: http://localhost:8080/swagger-ui.html

![image](https://raw.githubusercontent.com/mars05/static/master/image/crud3.gif)

### 2.项目右键单击生成代码:

![image](https://raw.githubusercontent.com/mars05/static/master/image/crud4.gif)