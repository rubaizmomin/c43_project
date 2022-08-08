import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.http.HttpResponse;

public class BookingRouter extends RequestRouter{

    public String url = "http://localhost:8007";

    @Override
    public void handleGet(HttpExchange r) throws JSONException, IOException, InterruptedException {
        String[] splitUrl = r.getRequestURI().getPath().split("/");
        System.out.println(r.getRequestURI().toString());
        if(splitUrl.length != 3){
            this.sendStatus(r, 400);
            return;
        }
        String actionString = splitUrl[1];
        switch (actionString) {
            // 8000/rent/rentlisting/:l_id
            case "bookings":
                try {
                    System.out.println(splitUrl[2].toString());
                    Integer u_id = Integer.parseInt(splitUrl[2]);
                } catch (Exception e) {
                    System.out.println("Cannot parse the uid in rentlisting/:lid");
                    this.sendStatus(r, 500);
                    return;
                }
                HttpResponse<String> s = this.sendRequestPost(r.getRequestMethod(), url, r.getRequestURI().toString(), "{}");

                this.sendResponse(r, new JSONObject(s.body()), s.statusCode());
                return;
            default:
                this.sendStatus(r, 400);
                break;
        }
    }
}
