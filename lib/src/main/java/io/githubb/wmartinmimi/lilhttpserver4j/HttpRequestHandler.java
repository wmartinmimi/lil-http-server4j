package io.githubb.wmartinmimi.lilhttpserver4j;

import io.githubb.wmartinmimi.liltcpserver4j.TCPRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HttpRequestHandler extends TCPRequestHandler {

  static final Logger logger = LoggerFactory.getLogger(HttpRequestHandler.class);

  HttpRequestListener httpRequestListener;

  public HttpRequestHandler(int port) throws IOException {
    this(Executors.newVirtualThreadPerTaskExecutor(), port);
  }

  public HttpRequestHandler(Executor executor, int port) throws IOException {
    super(executor, port);
    super.setTCPRequestListener((socket) -> {
      try (var scanner = new Scanner(socket.getInputStream())) {
        StringBuilder message = new StringBuilder(scanner.nextLine());
        var seg = message.toString().split(" ");
        var method = seg[0];
        var path = seg[1];
        message.append('\n');
        String line;
        while ((line = scanner.nextLine()).length() != 0)
          message.append(line).append("\n");
        logger.info("Received {} request for {}", method, path);
        assert httpRequestListener != null;
        var output = httpRequestListener.onHttpRequest(new HttpRequestRecord(method, path, message.toString()));
        try (var buffer = socket.getOutputStream()) {
          buffer.write(output.getHttpResponse());
          buffer.flush();
        }
      } catch (IOException e) {
        logger.error("socket error", e);
      }
    });
  }

  public void setHttpRequestListener(HttpRequestListener httpRequestListener) {
    this.httpRequestListener = httpRequestListener;
  }
}