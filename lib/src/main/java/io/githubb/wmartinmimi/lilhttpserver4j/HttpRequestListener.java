package io.githubb.wmartinmimi.lilhttpserver4j;

public interface HttpRequestListener {

  HttpResponse onHttpRequest(HttpRequestRecord request);

}
