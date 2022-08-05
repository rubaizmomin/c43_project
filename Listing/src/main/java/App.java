import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.SQLException;

public class App {
    static int PORT = 8002;
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", PORT), 0);
        // TODO: Add server contexts here. Do not set executors for the server, you shouldn't need them.
        System.out.println("I am here");
        server.createContext("/listing/addavailable", new Available());
        server.createContext("/listing/add", new AddListing());
//        server.createContext("/user/register", new Register());
        server.start();
        System.out.printf("Server started on port %d...\n", PORT);
    }
}