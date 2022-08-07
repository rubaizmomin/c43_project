import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {

    static int PORT = 8000;
    public static void main(String [] args) throws IOException {
        String hostname = "0.0.0.0";
        int backlog = 0;
        HttpServer server = HttpServer.create(new InetSocketAddress(hostname, PORT), backlog);
        server.createContext("/user", new UserRouter());
        server.createContext("/listing", new ListingRouter());
        server.createContext("/host", new HostRouter());
        server.createContext("/amenity", new AmenityRouter());
        server.createContext("/rent", new RenterRouter());
        server.createContext("/search", new SearchRouter());
        server.start();
        System.out.printf("Server started on port %d...\n", PORT);
    }
}

