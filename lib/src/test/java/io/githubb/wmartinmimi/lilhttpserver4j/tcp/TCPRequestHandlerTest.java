package io.githubb.wmartinmimi.lilhttpserver4j.tcp;

import io.githubb.wmartinmimi.liltcpserver4j.TCPRequestHandler;
import io.githubb.wmartinmimi.liltcpserver4j.TCPRequestListener;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TCPRequestHandlerTest {

  ExecutorService executor;
  TCPRequestHandler server;
  Socket client;

  @BeforeAll
  void setup() {
    executor = Executors.newVirtualThreadPerTaskExecutor();
  }

  @BeforeEach
  void setupEach() throws IOException {
    server = new TCPRequestHandler(executor, 20000);
    client = new Socket();
  }

  @AfterEach
  void clearUpEach() throws IOException {
    server.close();
    client.close();
  }

  @AfterAll
  void clearUp() {
    executor.shutdownNow();
  }

  @Test
  void messageTest() throws IOException {
    server.setTCPRequestListener((socket) -> {
      assertDoesNotThrow(() -> {
        try (var output = socket.getOutputStream()) {
          output.write("hello testing...".getBytes());
          output.flush();
        }
      });
    });
    executor.execute(server);
    client.connect(new InetSocketAddress("localhost", 20000));
    assertEquals("hello testing...", new String(client.getInputStream().readAllBytes()));
  }
}
