package io.githubb.wmartinmimi.lilhttpserver4j;

public final class HttpResponses {

  public static final class Status200 implements HttpResponse {

    String contentType;
    String body;

    public Status200(String contentType, String body) {
      this.contentType = contentType;
      this.body = body;
    }
    @Override
    public byte[] getHttpResponse() {
      return String.format("""
          HTTP/1.1 200 OK
          Content-Type: %s
          Content-Length: %d
          
          %s""",
          contentType,
          body.length(),
          body).getBytes();
    }
  }

  public static final class Status404 implements HttpResponse {

    String contentType;
    String body;

    public Status404(String contentType, String body) {
      this.contentType = contentType;
      this.body = body;
    }
    @Override
    public byte[] getHttpResponse() {
      return String.format("""
          HTTP/1.1 404 Not Found
          Content-Type: %s
          Content-Length: %d
          
          %s""",
          contentType,
          body.length(),
          body).getBytes();
    }
  }

  public static final class Status204 implements HttpResponse {

    @Override
    public byte[] getHttpResponse() {
      return "HTTP/1.1 204 No Content".getBytes();
    }
  }
}
