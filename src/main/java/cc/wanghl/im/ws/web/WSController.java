package cc.wanghl.im.ws.web;

import cc.wanghl.im.ws.web.domain.Command;
import cc.wanghl.im.ws.web.domain.Response;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint(value = "/message_websocket")
@Controller
public class WSController {
    private Map<String,Session> sessionMap = new HashMap<>();
    private static final Logger logger = LogManager.getLogger(WSController.class);
    @OnOpen
    public void onOpen(Session session) {
        sessionMap.put(session.getId(), session);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        System.out.println("session close. ID:" + session.getId());
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        Command c = JSONObject.parseObject(message, Command.class);
        c.setFrom(session.getId());
        boolean r = sendToUser(c.getTo(),JSONObject.toJSONString(c));
        if(!r){
            sendToUser(session.getId(), Response.gen("01","用户["+c.getTo()+"]不存在或没有上线").toJSON());
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 发送文本给指定用户
     * @param to 发送对像的ID
     * @param message 待发送对像（文本）
     * @return
     * @throws IOException
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
