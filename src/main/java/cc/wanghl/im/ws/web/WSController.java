package cc.wanghl.im.ws.web;

import cc.wanghl.im.ws.web.domain.Command;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.web.bind.annotation.RequestBody;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
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
    public void onMessage(String message, Session session) {
        Command c = JSONObject.parseObject(message, Command.class);
        logger.debug(c);
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
}
