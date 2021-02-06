package com.kalew515.pestmessageboardbackend.controller;

import com.kalew515.pestmessageboardbackend.util.ApplicationContextTool;
import com.kalew515.pestmessageboardbackend.util.RedisTool;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/websocket/{id}")
@Component
public class WebSocketController {

    public WebSocketController webSocketController;

    private static AtomicInteger onlineCount = new AtomicInteger(0);

    private static Map<String, Session> clients = new ConcurrentHashMap<>();

    private String id;

    @OnOpen
    public void onOpen (Session session, @PathParam("id") String id) {
        onlineCount.incrementAndGet();
        clients.put(session.getId(), session);
        this.id = id;
    }

    @OnClose
    public void onClose (Session session) {
        onlineCount.decrementAndGet();
        clients.remove(session.getId());
    }

    @OnMessage
    public void onMessage (String message, Session session) {
        this.sendMessage(session);
    }

    public RedisTool getRedisTool () {
        return (RedisTool) ApplicationContextTool.getBean("redisTool");
    }

    private void sendMessage (Session toSession) {
        try {
            JSONObject jsonObject = new JSONObject();
            Integer newReply, newLike, newDislike;
            RedisTool redis = this.getRedisTool();
            try {
                newReply = Integer.parseInt(redis.hget("info" + this.id, "nreply")
                                                 .toString());
            } catch (NullPointerException e) {
                newReply = 0;
            }
            try {
                newLike = Integer.parseInt(redis.hget("info" + this.id, "nlike")
                                                .toString());
            } catch (NullPointerException e) {
                newLike = 0;
            }
            try {
                newDislike = Integer.parseInt(redis.hget("info" + this.id, "ndislike")
                                                   .toString());
            } catch (NullPointerException e) {
                newDislike = 0;
            }
            jsonObject.put("newReply", newReply);
            jsonObject.put("newLike", newLike);
            jsonObject.put("newDislike", newDislike);
            toSession.getBasicRemote()
                     .sendText(jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
