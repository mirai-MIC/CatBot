# MusicCatBot

基于simbot3.0框架新版的QQ机器人

### 环境(全部基于测试环境)

| 工具           | 版本      |
|:-------------|---------|
| Jdk          | 17      |
| SpringBoot   | 3.0     |
| mybatis-plus | 3.5.3.1 |
| mysql        | 8.0.3.2 |
| Maven        | 3.8.7   |


### 项目结构

```yml
├─cache
├─lib
└─src
└─main
├─java
│  └─org
│      └─Simbot
│          ├─listens
│          │  └─data
│          ├─mybatisplus
│          │  ├─domain
│          │  └─mapper
│          ├─plugins
│          │  ├─admin
│          │  │  └─Adminutils
│          │  ├─card
│          │  ├─menu
│          │  ├─music
│          │  │  └─data
│          │  ├─news
│          │  ├─openai
│          │  │  └─data
│          │  ├─searchImage
│          │  ├─searchvideo
│          │  │  └─data
│          │  ├─sign
│          │  ├─tu
│          │  │  └─Data
│          │  ├─video
│          │  │  └─data
│          │  ├─xiaoai
│          │  │  └─Xutils
│          │  ├─yiyan
│          │  └─youthlearning
│          │      └─Data
│          ├─startup
│          ├─time
│          └─utils
│              └─Properties
└─resources
└─simbot-bots

```

### 功能预览

| 功能                    | 命令                         |
|:----------------------|----------------------------|
| 注册签到                  | 注册                         |
| 群签到                   | 签到                         |
| 查看积分                  | /积分                        |
| 青年大学习                 | /青年大学习                     |
| 点歌                    | /点歌 歌名                     |
| 搜番功能                  | 搜番                         |
| 搜图功能（仅限p站图片）          | 搜图                         |
| 色图功能                  | /all                       |
| 进群欢迎                  | 主动功能                       |
| 踢出或退群提醒               | 主动功能                       |
| 移除群成员(Bot需要管理权限)      | k/{{accountCode}}          |
| 禁言群成员(Bot需要管理权限)      | b/{{accountCode}}/{{Time}} |
| 解除禁言状态(Bot需要管理权限)     | j/{{Id}}                   |
| 小爱同学聊天                | @bot即可                     |
| 发送卡片消息                | /c {{卡片代码}}                |
| 转发消息                  | 转发/{{群号}}/{{消息}}           |
| 腾讯视频搜索                | /tx {{电视电影名}}/{{集数}}       |
| openai问答(当前权限只开放给拥有者) | /q {{text}}                |
| 解析小程序QQ(bilibili)     | 主动功能                       |
| 每日早报                  | /每日早报 or /早报               |
| Bot菜单                 | /菜单                        |

### 开发文档

https://simbot.forte.love/

### fork/clone

fork或者clone此项目到你的本地，并使用IDE工具打开并构建它。

## 修改配置文件

**simbot.bot.json**

- 账号配置[bot.json](src/main/resources/simbot-bots/simbot.bot.json)

```yml
{
  "component": "simbot.mirai",
  "code": qq号,
  "passwordInfo": {
    "type": "text",
    "text": "密码"
  },
  "config": {
    "protocol": "MACOS",
    "deviceInfo": {
      "type": "file_based"
    }
  }
}
```

**application.yml**

- 数据库配置[application.yml](src/main/resources/application.yml)

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

## apiKey配置

**application.properties**

- 修改配置 [application.properties](cache/application.properties)

```properties
user.Master=拥有者QQ
user.openai=openAi官方Api
user.searchImage=saucenao.com注册并复制key
```

### 阅读

- [listener](src/main/java/org/Simbot/listens/ListenGroup.java) 包下为一些监听函数示例。阅读它们的注释，并可以试着修改它们。

### 运行

执行[SimbotApp](src/main/java/org/Simbot/SimbotApp.java) 中的main方法。

### 验证

如果第一次登陆启动程序 会出现滑块验证

滑块验证助手：https://install.appcenter.ms/users/mzdluo123/apps/txcaptchahelper/distribution_groups/public

将链接复制到软件里验证复制token继续粘贴验证

## 这是我的交流群

QQ群: 620428906
欢迎各位大佬来玩
