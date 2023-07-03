package io.githubb.wmartinmimi.lilhttpserver4j.http;

import io.githubb.wmartinmimi.lilhttpserver4j.HttpRequestHandler;
import io.githubb.wmartinmimi.lilhttpserver4j.HttpRequestListener;
import io.githubb.wmartinmimi.lilhttpserver4j.HttpResponses;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpRequestHandlerTest {

  ExecutorService executor;
  HttpRequestHandler server;
  Socket client;

  @BeforeAll
  void setup() {
    executor = Executors.newVirtualThreadPerTaskExecutor();
  }

  @BeforeEach
  void startServer() throws IOException {
    server = new HttpRequestHandler(executor, 20000);
    client = new Socket();
  }

  @AfterEach
  void stopServer() throws IOException {
    server.close();
    client.close();
  }

  @AfterAll
  void clearup() {
    executor.shutdownNow();
  }


  @Test
  void status200Test() throws IOException {
    HttpRequestListener listener = (request) -> {
      assertEquals("GET", request.method());
      assertEquals("/hello", request.path());
      return new HttpResponses.Status200("text/plain-text", "hello");
    };
    server.setHttpRequestListener(listener);
    executor.execute(server);
    client.connect(new InetSocketAddress("localhost", 20000));
    client.getOutputStream().write("GET /hello HTTP/1.1\n\n\n".getBytes());
    client.getOutputStream().flush();
    assertEquals("""
        HTTP/1.1 200 OK
        Content-Type: text/plain-text
        Content-Length: 5
                  
        hello""", new String(client.getInputStream().readAllBytes()));
  }


}
