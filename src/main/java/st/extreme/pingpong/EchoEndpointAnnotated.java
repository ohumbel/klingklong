package st.extreme.pingpong;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/echo")
public class EchoEndpointAnnotated {
    @OnMessage
    public String onMessage(String message, Session session) {
        return message;
    }
}


