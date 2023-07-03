package io.githubb.wmartinmimi.liltcpserver4j;

import java.net.Socket;

public interface TCPRequestListener {

  void onTCPRequest(Socket socket);
}
