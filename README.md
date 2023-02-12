# MusicCatBot
基于simbot3.0框架新版的QQ机器人
# 这是一个simbot框架的机器人项目




## 需要做的
### 开发文档
https://simbot.forte.love/

### fork/clone
fork或者clone此项目到你的本地，并使用IDE工具打开并构建它。

### 修改配置文件

打开目录 【src/main/resources/simbot.yml】

根据目录下的文件调整、删改，修改为你的bot的配置信息。

**simbot.bot.json**

```yml
{
  "component": "simbot.mirai",
  "code": qq号,
  "passwordInfo": {
    "type": "text",
    "text": "密码"
  },
  "config": {
    "protocol": "IPAD",
    "deviceInfo": {
      "type": "file_based"
    }
  }
}
```
**application.yml**

```yml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: username
    password: password
    url: jdbc:mysql://localhost:3306/signsql
#    将数据库账户密码替换自己的
#    数据库文件: src/main/resources/signsql.sql

```


### 阅读
- [listener](src/main/java/love/simbot/example/listener) 包下为一些监听函数示例。阅读它们的注释，并可以试着修改它们。

### 运行
执行[SimbotApp](src/main/java/org/Simbot/SimbotApp.java) 中的main方法。

### 验证
如果第一次登陆启动程序 会出现滑块验证

滑块验证助手：https://install.appcenter.ms/users/mzdluo123/apps/txcaptchahelper/distribution_groups/public

将链接复制到软件里验证复制token继续粘贴验证

### 功能预览
* 群签到
* 抽签
* 搜图 需要 saucenao网站的 apikey
* 搜番 
* 发送色图
* 点歌
* 解析腾讯视频
* B站小程序解析
* openAI
* 青年大学习
* 小爱同学聊天
* 基础的管理员插件


## 这是我的交流群
QQ群: 620428906
欢迎各位大佬来玩
