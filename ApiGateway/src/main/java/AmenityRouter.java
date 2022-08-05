import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.http.HttpResponse;

public class AmenityRouter extends RequestRouter {
    public String url = "http://localhost:8004";
    @Override
    public void handlePost(HttpExchange r) throws JSONException, IOException, InterruptedException {
        String[] splitUrl = r.getRequestURI().getPath().split("/");
        System.out.println(r.getRequestURI().toString());
        if (splitUrl.length != 3) {
            this.sendStatus(r, 400);
            return;
        }
        String actionString = splitUrl[2];
        switch (actionString){
            case "add":
                System.out.println("3");
                String bodyStr = Utils.convert(r.getRequestBody());
                JSONObject bodyJson = new JSONObject(bodyStr);
                String bodyJsonStr = bodyJson.toString();
                System.out.println(bodyJsonStr);
                HttpResponse<String> s = this.sendRequestPost(r.getRequestMethod(), url, r.getRequestURI().toString(), bodyJsonStr);
                System.out.println(s.body());
                this.sendResponse(r, new JSONObject(s.body()), s.statusCode());
                return;
            default:
                this.sendStatus(r, 400);
                break;
        }
    }
}
