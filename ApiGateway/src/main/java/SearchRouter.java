import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.http.HttpResponse;

public class SearchRouter extends RequestRouter{
    public String url = "http://localhost:8006";
    @Override
    public void handleGet(HttpExchange r) throws JSONException, IOException, InterruptedException {
        String[] splitUrl = r.getRequestURI().getPath().split("/");
        System.out.println("I am here");
        if(splitUrl.length != 3){
            this.sendStatus(r, 400);
            System.out.println("Check url");
            return;
        }
        if(splitUrl.length == 3) {
            try {
                String[] uriPartsP2 = r.getRequestURI().toString().split("\\?");
                for(int i = 0; i < uriPartsP2.length; i++){
                    System.out.println(uriPartsP2[i]);
                }
                System.out.println("? delimiter was above");
                String [] actionString = uriPartsP2[0].split("/");
                for(int i = 0; i < actionString.length; i++){
                    System.out.println(actionString[i]);
                }
                System.out.println("This is action[1]" + actionString[1]);
                switch (actionString[2]) {
                    // 8000/rentavailable/:l_id
                    case "listing":
                        String bodyStr = Utils.convert(r.getRequestBody());
                        JSONObject bodyJson = new JSONObject(bodyStr);
                        String bodyJsonStr = bodyJson.toString();
                        System.out.println(bodyJsonStr);
                        HttpResponse<String> s = this.sendRequestPost(r.getRequestMethod(), url, r.getRequestURI().toString(), bodyJsonStr);
                        System.out.println(s.body());
                        this.sendResponse(r, new JSONObject(s.body()), s.statusCode());
                        return;
                }
            } catch (Exception e){
            System.out.println("search Router");
            this.sendStatus(r, 500);
            return;
        }
        }
    }
}
