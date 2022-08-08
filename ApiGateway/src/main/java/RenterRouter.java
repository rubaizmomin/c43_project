import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.http.HttpResponse;

public class RenterRouter extends RequestRouter{
    public String url = "http://localhost:8005";


    @Override
    public void handleGet(HttpExchange r) throws JSONException, IOException, InterruptedException {
        String[] splitUrl = r.getRequestURI().getPath().split("/");
        System.out.println(r.getRequestURI().toString());
        if (splitUrl.length != 2 && splitUrl.length != 4) {
            this.sendStatus(r, 400);
            return;
        }
        if(splitUrl.length == 4){
            String actionString = splitUrl[2];
            switch (actionString){
                // 8000/rent/rentlisting/:l_id
                case "rentlisting":
                    try{
                        Integer u_id = Integer.parseInt(splitUrl[3]);
                    } catch (Exception e){
                        System.out.println("Cannot parse the uid in rentlisting/:lid");
                        this.sendStatus(r, 500);
                        return;
                    }
                    JSONObject bodyJson = new JSONObject();
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
    @Override
    public void handlePost(HttpExchange r) throws JSONException, IOException, InterruptedException {
        String[] splitUrl = r.getRequestURI().getPath().split("/");
        System.out.println(r.getRequestURI().toString());
        if (splitUrl.length != 4) {
            this.sendStatus(r, 400);
            return;
        }
        if(splitUrl.length == 4) {
            String actionString = splitUrl[2];
            switch (actionString) {
                //uri:8000/rent
                case "book":
                    try{
                        Integer u_id = Integer.parseInt(splitUrl[3]);
                    } catch (Exception e){
                        System.out.println("Cannot parse the uid in rentlisting/:lid");
                        this.sendStatus(r, 500);
                        return;
                    }
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
}
