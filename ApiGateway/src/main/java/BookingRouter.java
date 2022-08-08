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
        if (splitUrl.length != 3) {
            this.sendStatus(r, 400);
            return;
        }
        if (splitUrl.length == 3) {
            String actionString = splitUrl[1];
            switch (actionString) {
                // 8000/bookings/:uid
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
    @Override
    public void handlePost(HttpExchange r) throws JSONException, IOException, InterruptedException {
        String[] splitUrl = r.getRequestURI().getPath().split("/");
        System.out.println(r.getRequestURI().toString());
        if (splitUrl.length != 4) {
            this.sendStatus(r, 400);
            return;
        }
        if (splitUrl.length == 4) {
            String actionString = splitUrl[2];
            switch (actionString) {
                // 8000/bookings/:uid
                case "addreview":
                case "cancelbooking":
                    String uri = r.getRequestURI().toString();
                    splitUrl = uri.split("/");
                    String[] uriPartsP2 = splitUrl[3].split("\\?");
                    Integer l_id = Integer.parseInt(uriPartsP2[0]);
                    String[] uriPartsP3queryStr = uriPartsP2[1].split("=");
                    Integer u_id = Integer.parseInt(uriPartsP3queryStr[1]);
                    String bodyStr = Utils.convert(r.getRequestBody());
                    JSONObject bodyJson = new JSONObject(bodyStr);
                    String bodyJsonStr = bodyJson.toString();
                    HttpResponse<String> s = this.sendRequestPost(r.getRequestMethod(), url, r.getRequestURI().toString(), bodyJsonStr);
                    this.sendResponse(r, new JSONObject(s.body()), s.statusCode());
                    return;
                default:
                    this.sendStatus(r, 400);
                    break;
            }
        }
    }
}
