package io.githubb.wmartinmimi.liltcpserver4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TCPRequestHandler implements Runnable, AutoCloseable{

  static final Logger logger = LoggerFactory.getLogger(TCPRequestHandler.class);

  TCPRequestListener tcpRequestListener;
  Executor executor;
  ServerSocket serverSocket;
  boolean isRunning;

  public TCPRequestHandler(int port) throws IOException {
    this(Executors.newVirtualThreadPerTaskExecutor(), port);
  }

  public TCPRequestHandler(Executor executor, int port) throws IOException {
    serverSocket = new ServerSocket(port);

    this.executor = executor;
  }

  public void setTCPRequestListener(TCPRequestListener listener) {
    this.tcpRequestListener = listener;
  }

  @Override
  public void run() {
    isRunning = true;
    logger.info("Request handler started!");
    logger.info("Listening on port {}", serverSocket.getLocalPort());
    try {
      while (isRunning) {
        var socket = serverSocket.accept();
        logger.info("Accepted connection from {}", socket.getRemoteSocketAddress());
        assert tcpRequestListener != null;
        executor.execute(() -> tcpRequestListener.onTCPRequest(socket));
      }
    } catch (IOException e) {
      isRunning = false;
    }
  }

  @Override
  public void close() throws IOException {
    logger.info("Request handler closing...");
    isRunning = false;
    serverSocket.close();
    logger.info("Request handler closed!");
  }
}