package utils;

import lombok.Getter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class HttpClientUtil {
    @Getter
    private static final HttpClient httpClient = initHttpClient();

    protected static HttpClient initHttpClient() {
        return HttpClient.newHttpClient();
    }


    public static String GET(URI uri) throws InterruptedException, IOException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
