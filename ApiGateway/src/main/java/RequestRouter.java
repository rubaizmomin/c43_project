import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class RequestRouter implements HttpHandler {
    public HashMap<Integer, String> errorMap;
    public RequestRouter() {
        errorMap = new HashMap<>();
        errorMap.put(200, "OK");
        errorMap.put(400, "BAD REQUEST");
        errorMap.put(401, "UNAUTHORIZED");
        errorMap.put(404, "NOT FOUND");
        errorMap.put(405, "METHOD NOT ALLOWED");
        errorMap.put(409, "CONFLICT");
        errorMap.put(500, "INTERNAL SERVER ERROR");
    }

    public void handle(HttpExchange r) throws IOException {
        r.getResponseHeaders().add("Access-Control-Allow-Origin", "*"); // For CORS
        try {
            switch (r.getRequestMethod()) {
                case "OPTIONS":
                    this.handleCors(r);
                    break;
                case "GET":
                    this.handleGet(r);
                    break;
                case "PATCH":
                    this.handlePatch(r);
                    break;
                case "POST":
                    this.handlePost(r);
                    break;
                case "PUT":
                    this.handlePut(r);
                    break;
                case "DELETE":
                    this.handleDelete(r);
                    break;
                default:
                    this.sendStatus(r, 405);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HttpResponse<String> sendRequest(String method, String url, String uri, String body) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .method(method, HttpRequest.BodyPublishers.ofString(body))
                .uri(URI.create(url + uri))
                .build();
        return client.send(req, HttpResponse.BodyHandlers.ofString());
    }
    public HttpResponse<String> sendRequestPatch(String method, String url, String uri, String body) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .method(method, HttpRequest.BodyPublishers.ofString(body))
                .uri(URI.create(url + uri))
                .build();
        return client.send(req, HttpResponse.BodyHandlers.ofString());
    }
    public HttpResponse<String> sendRequestPost(String method, String url, String uri, String body) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .method(method, HttpRequest.BodyPublishers.ofString(body))
                .uri(URI.create(url + uri))
                .build();
        //HANGS INDEFINITELY
        System.out.println("4");
        return client.send(req, HttpResponse.BodyHandlers.ofString());
    }
    public HttpResponse<String> sendRequestPut(String method, String url, String uri, String body) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .method(method, HttpRequest.BodyPublishers.ofString(body))
                .uri(URI.create(url + uri))
                .build();
        return client.send(req, HttpResponse.BodyHandlers.ofString());
    }
    public HttpResponse<String> sendRequestDelete(String method, String url, String uri, String body) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .method(method, HttpRequest.BodyPublishers.ofString(body))
                .uri(URI.create(url + uri))
                .build();
        return client.send(req, HttpResponse.BodyHandlers.ofString());
    }
    public void handleCors(HttpExchange r) throws IOException {
        r.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
        r.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
        r.sendResponseHeaders(204, -1);
        return;
    }
    public void sendStatus(HttpExchange r, int statusCode) throws JSONException, IOException {
        JSONObject res = new JSONObject();
        res.put("status", errorMap.get(statusCode));
        String response = res.toString();
        r.sendResponseHeaders(statusCode, response.length());
        this.writeOutputStream(r, response);
    }

    public void sendResponse(HttpExchange r, JSONObject obj, int statusCode) throws JSONException, IOException {
        obj.put("status", errorMap.get(statusCode));
        String response = obj.toString();
        r.sendResponseHeaders(statusCode, response.length());
        this.writeOutputStream(r, response);
    }
    public void writeOutputStream(HttpExchange r, String response) throws IOException {
        OutputStream os = r.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    public void handleGet(HttpExchange r) throws JSONException, IOException, InterruptedException {};
    public void handlePatch(HttpExchange r) {};
    public void handlePost(HttpExchange r) throws JSONException, IOException, InterruptedException {};
    public void handlePut(HttpExchange r) {};
    public void handleDelete(HttpExchange r){};
}

