package cc.wanghl.im.ws.web;

import cc.wanghl.im.ws.web.domain.Command;
import cc.wanghl.im.ws.web.domain.Response;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint(value = "/message_websocket/{username}")
@Controller
public class WSController {
    private static final Map<String,Session> sessionMap = new HashMap<>();
    private static final Logger logger = LogManager.getLogger(WSController.class);

    @OnOpen
    public void onOpen(Session session,@PathParam("username") String username) throws IOException {
        logger.debug(username + " connected!");
        if(sessionMap.get(username) != null){
            Command c = new Command();
            c.setFrom("server");
            c.setTo(username);
            c.setAction("disconnected");
            c.setData("You are logged in on another device, go offline here!");
            sendToUser(username,JSONObject.toJSONString(c));
            sessionMap.get(username).close();
        }

        Command c = new Command();
        c.setFrom("server");
        c.setTo(username);
        c.setAction("connected");
        sessionMap.put(username,session);
        sendToUser(username,JSONObject.toJSONString(c));
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session,@PathParam("username") String username) {
        sessionMap.remove(username);
        logger.debug("session close. username:" + username);
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session,@PathParam("username") String username) throws IOException {
        try{
            Command c = JSONObject.parseObject(message, Command.class);
            c.setFrom(session.getId());
            boolean r = sendToUser(c.getTo(),JSONObject.toJSONString(c));
            if(!r){
                sendToUser(session.getId(), Response.gen("01","用户["+c.getTo()+"]不存在或没有上线").toJSON());
            }
        }catch (Exception e){
            logger.error(e);

            Command c = new Command();
            c.setFrom("server");
            c.setTo(username);
            c.setAction("error");
            c.setData(e.getMessage());
            sendToUser(username, JSONObject.toJSONString(c));
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error,@PathParam("username") String username) {
        logger.error(username + "occurred an error", error);
    }

    /**
     * 发送文本给指定用户
     * @param to 发送对像的ID
     * @param message 待发送对像（文本）
     * @return 是否发送成功
     * @throws IOException 发送失败异常
     */
    private boolean sendToUser(String to ,String message) throws IOException {
        boolean r = false;
        if(sessionMap.get(to) != null){
            sessionMap.get(to).getBasicRemote().sendText(message);
            r = true;
        }
        return r;
    }
}
