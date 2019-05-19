package st.extreme.klingklong;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/echo")
public class EchoEndpointAnnotated {
  @OnMessage
  public void onMessage(String message, Session session) {
    System.out.println("Server onMessage: >" + message + "<");
    try {
      TimeUnit.SECONDS.sleep(2);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Server sending message: >" + message + "<");
    try {
      session.getBasicRemote().sendText(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
