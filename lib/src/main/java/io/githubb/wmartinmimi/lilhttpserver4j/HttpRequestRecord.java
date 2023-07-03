package io.githubb.wmartinmimi.lilhttpserver4j;

public record HttpRequestRecord(String method, String path, String body) {
}