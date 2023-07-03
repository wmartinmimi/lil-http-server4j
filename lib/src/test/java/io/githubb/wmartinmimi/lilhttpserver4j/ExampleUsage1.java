package io.githubb.wmartinmimi.lilhttpserver4j;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.net.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExampleUsage1 {

  @Test
  void run() throws URISyntaxException {
    try (var server = new HttpRequestHandler(20000)) {
      server.setHttpRequestListener((request) -> switch (request.path()) {
        case "/hello" -> new HttpResponses.Status200("text/plain-text", "hello");
        case "/bye" -> new HttpResponses.Status204();
        default -> throw new IllegalStateException("Unexpected value: " + request.path());
      });
      Thread.ofVirtual().start(server);

      // test http://localhost:20000/hello endpoint
      var client = new URI("http://localhost:20000/hello").toURL();
      var connection = (HttpURLConnection) client.openConnection();
      connection.setRequestMethod("GET");
      assert connection.getResponseCode() == 200;
      assertEquals("hello", new String(connection.getInputStream().readAllBytes()));

      // test http://localhost:20000/bye endpoint
      client = new URI("http://localhost:20000/bye").toURL();
      connection = (HttpURLConnection) client.openConnection();
      connection.setRequestMethod("GET");
      assert connection.getResponseCode() == 204;

    } catch (IOException ignored) {
    }

  }

}
