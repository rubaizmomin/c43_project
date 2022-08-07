import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.http.HttpResponse;

public class ListingRouter extends RequestRouter{
    public String url = "http://localhost:8002";
    @Override
    public void handlePost(HttpExchange r) throws JSONException, IOException, InterruptedException {
        String uri = r.getRequestURI().toString();
        String[] splitUrl = uri.split("/");
        System.out.println(r.getRequestURI().toString());
        if (splitUrl.length != 3 && splitUrl.length != 4) {
            this.sendStatus(r, 400);
            return;
        }
        if(splitUrl.length == 3) {
            String actionString = splitUrl[2];
            switch (actionString) {
                case "addlisting":
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
        if(splitUrl.length == 4){
            if(splitUrl[2].equals("addavailable"))
            {
                try{
                    String[] uriPartsP2 = splitUrl[3].split("\\?");

                    if (uriPartsP2.length != 2) {
                        System.out.println("check uri for addavailable");
                        this.sendStatus(r, 400);
                        return;
                    }
                    Integer u_id = Integer.parseInt(uriPartsP2[0]);
                    String[] uriPartsP3queryStr = uriPartsP2[1].split("=");
                    if (uriPartsP3queryStr.length != 2 && !uriPartsP3queryStr[0].equals("l_id")) {
                        System.out.println("Check the last part of addavailable");
                        this.sendStatus(r, 400);
                        return;
                    }
                    Integer l_id = Integer.parseInt(uriPartsP3queryStr[1]);
                    String bodyStr = Utils.convert(r.getRequestBody());
                    JSONObject bodyJson = new JSONObject(bodyStr);
                    String bodyJsonStr = bodyJson.toString();
                    System.out.println(bodyJsonStr);
                    HttpResponse<String> s = this.sendRequestPost(r.getRequestMethod(), url, r.getRequestURI().toString(), bodyJsonStr);
                    System.out.println(s.body());
                    this.sendResponse(r, new JSONObject(s.body()), s.statusCode());
                    return;
                }catch (Exception e){
                    System.out.println("l_id is not int in listing/available/:l_id");
                    this.sendStatus(r, 500);
                    return;
                }
            }
        }
    }
    @Override
    public void handleGet(HttpExchange r) throws JSONException, IOException, InterruptedException {
        String uri = r.getRequestURI().toString();
        String[] splitUrl = uri.split("/");
        if(splitUrl.length != 4){
            this.sendStatus(r, 400);
            System.out.println("Check url for addavailable GET");
            return;
        }
        String actionString = splitUrl[2];
        switch (actionString) {
            case "addavailable":
                try{
                    Integer u_id = Integer.parseInt(splitUrl[3]);
                } catch (Exception e){
                    System.out.println("Cannot parse the uid in addavailable/uid");
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
