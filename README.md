# lil-http-server4j

An experimental http server for utilising virtual thread features.

## IMPORTANT!

Not for production uses!

## Example Usage

```java
class Main {
  
  public static void main(String[] args) throws IOException {
    try (var server = new HttpRequestHandler(20000)) {
      server.setHttpRequestListener((request) -> switch (request.path()) {
        case "/hello" -> new HttpResponses.Status200("text/plain-text", "hello");
        case "/bye" -> new HttpResponses.Status204();
        default -> new HttpResponses.Status404("text/plain-text", "404 error");
      });
      Thread.ofVirtual().start(server);
    }
  }
}
```

- [Example Usage 1](https://github.com/wmartinmimi/lil-http-server4j/blob/main/lib/src/test/java/io/githubb/wmartinmimi/lilhttpserver4j/ExampleUsage1.java)

## Features

- Uses Virtual Threads

## Requirement

- JDK version 20+

## Development

### Testing

```shell
./gradlew test
```

### Building

```shell
./gradlew build
```

## License

Licensed under ```MIT License```
