package com.goit.javacore.module13.pshcherba.client;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@AllArgsConstructor
public class JsonHttpService {

    private final HttpClient httpClient;


    public HttpRequest createGetRequest(String strUri)  {
        URI uri = URI.create(strUri);
        return HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
    }

    public HttpRequest createPostRequest(String json, String strUri) {
        URI uri = URI.create(strUri);
        return HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
    }

    public HttpRequest createPutRequest(String json, String strUri) {
        URI uri = URI.create(strUri);
        return HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
    }

    public HttpRequest createDeleteRequest(String strUri) {
        URI uri = URI.create(strUri);
        return HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
    }

    public HttpResponse<String> createResponse(HttpRequest request)  {
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
}
