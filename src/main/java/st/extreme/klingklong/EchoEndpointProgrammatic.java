package st.extreme.klingklong;

import java.io.IOException;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

public class EchoEndpointProgrammatic extends Endpoint {

  @Override
  public void onOpen(final Session session, EndpointConfig config) {
    session.addMessageHandler(new MessageHandler.Whole<String>() {
      @Override
      public void onMessage(String message) {
        try {
          session.getBasicRemote().sendText(message);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }

}