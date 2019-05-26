package st.extreme.klingklong;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

final class Sender extends Thread {

  static final String STOP_SIGNAL = "__^ 3B@?)8Eu6.x6u2t?7?cw#2e+2W8)P?R3 ^__";

  public Sender(InetAddress remoteHost, int sendingPort) {
    // TODO Auto-generated constructor stub
  }

  void send(String message) {

  }

  void close() {
    // TODO
  }

  void sendLocal(String message, int localReceivingPort) throws UnknownHostException, IOException {
    try (Socket localReceivingSocket = new Socket(InetAddress.getLocalHost(), localReceivingPort);
        PrintWriter out = new PrintWriter(localReceivingSocket.getOutputStream(), true)) {
      out.println(Message.forSending(message));
    }
  }

}
