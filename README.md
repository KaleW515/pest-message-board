# pest-message-board

- 一个简易的网络留言板,基本功能都写好了
- 使用docker快速部署
- 使用websocket进行消息推送
- 需要环境: npm, maven, docker, docker-compose

# evelopment Setup

```shell
1. cd pest-message-board-front/ 
2. npm install
3. npm run build
4. cd ..
5. cd pest-message-board-backend
6. mvn clean package
7. mv pest-message-board-backend-0.0.1-SNAPSHOT.jar ../
8. cd ..
9. docker-compose up
```



# front

vue

# backend

springboot
- 疫情信息展示接口可能用不了了,未更新
