package com.teng.mai.config;

import org.springframework.stereotype.Component;

import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * @description: 从websocket中获取用户session
 * @author: ~Teng~
 * @date: 2024/3/23 23:48
 */
@Component
public class HttpSessionConfigurator extends ServerEndpointConfig.Configurator implements ServletRequestListener {
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        if (httpSession != null) {
            sec.getUserProperties().put(HttpSession.class.getName(), httpSession);
        }
    }
}
